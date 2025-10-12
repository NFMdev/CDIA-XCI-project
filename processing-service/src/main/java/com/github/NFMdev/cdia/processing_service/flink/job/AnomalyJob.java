package com.github.NFMdev.cdia.processing_service.flink.job;

import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperationBuilders;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperationVariant;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.NFMdev.cdia.processing_service.flink.model.Event;
import com.github.NFMdev.cdia.processing_service.flink.model.EventAnomaly;
import com.github.NFMdev.cdia.processing_service.flink.function.AnomalyDetectionFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.dsv2.Source;
import org.apache.flink.cdc.connectors.base.source.jdbc.JdbcIncrementalSource;
import org.apache.flink.cdc.connectors.postgres.PostgreSQLSource;
import org.apache.flink.cdc.connectors.postgres.source.PostgresSourceBuilder;
import org.apache.flink.cdc.debezium.DebeziumDeserializationSchema;
import org.apache.flink.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch8AsyncSink;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch8AsyncSinkBuilder;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.connector.jdbc.core.datastream.sink.JdbcSink;
import org.apache.flink.connector.jdbc.core.datastream.source.JdbcSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.http.HttpHost;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public class AnomalyJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DebeziumDeserializationSchema<String> deserializer = new JsonDebeziumDeserializationSchema();

        JdbcIncrementalSource<String> postgresSource = PostgresSourceBuilder.PostgresIncrementalSource.<String>builder()
                .hostname("postgres")
                .port(5432)
                .database("crime_analytics")
                .schemaList("public")
                .tableList("public.events")
                .username("admin")
                .password("admin")
                .slotName("flink")
                .decodingPluginName("pgoutput")
                .deserializer(deserializer)
                .build();

//        JdbcSource<Event> jdbcSource = JdbcSource.<Event>builder()
//                .setSql("SELECT id, description, location, source_id, created_at FROM events")
//                .setDriverName("org.postgresql.Driver")
//                .setDBUrl("jdbc:postgresql://postgres:5432/crime_analytics")
//                .setUsername("admin")
//                .setPassword("admin")
//                .setTypeInformation(TypeInformation.of(Event.class))
//                .setResultExtractor(rs -> new Event(
//                        rs.getLong("id"),
//                        rs.getString("description"),
//                        rs.getString("location"),
//                        rs.getLong("source_id"),
//                        rs.getTimestamp("created_at")))
//                .build();

        DataStream<String> cdcStream = env.fromSource(
                postgresSource,
                WatermarkStrategy.noWatermarks(),
                "PostgresCDC"
        );

        DataStream<Event> events = cdcStream
                .map(json -> {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(json);

                    JsonNode after = node.get("after");
                    if (after == null || after.isNull()) return null;

                    Event event = new Event();
                    event.setId(after.get("id").longValue());
                    event.setLocation(after.get("location").asText());
                    event.setCreatedAt(Timestamp.from(Instant.parse(after.get("created_at").asText())));
                    return event;
                })
                .filter(Objects::nonNull);

        DataStream<EventAnomaly> anomalies = events
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
                        .withTimestampAssigner((event, ts) -> event.getCreatedAt().getTime()))
                .keyBy(Event::getLocation)
                .window(TumblingEventTimeWindows.of(
                        Duration.ofMinutes(1)))
                .process(new AnomalyDetectionFunction());

//        anomalies.sinkTo(JdbcSink.<EventAnomaly>builder()
//                .withQueryStatement(
//                        "INSERT INTO event_anomalies(id, location, event_count, window_start, window_end, " +
//                                "detected_at, rule, severity, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                        (JdbcStatementBuilder<EventAnomaly>) (stmt, anomaly) -> {
//                            stmt.setObject(1, anomaly.getId());
//                            stmt.setString(2, anomaly.getLocation());
//                            stmt.setLong(3, anomaly.eventCount);
//                            stmt.setTimestamp(4, anomaly.getWindowStart());
//                            stmt.setTimestamp(5, anomaly.getWindowEnd());
//                            stmt.setTimestamp(6, anomaly.getDetectedAt());
//                            stmt.setString(7, anomaly.getRule());
//                            stmt.setString(8, anomaly.getSeverity());
//                            stmt.setString(9, anomaly.getDescription());
//                        }).buildAtLeastOnce(
//                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
//                                .withUrl("jdbc:postgresql://postgres:5432/crime_analytics")
//                                .withDriverName("org.postgresql.Driver")
//                                .withUsername("admin")
//                                .withPassword("admin")
//                                .build()
//                ));


        Elasticsearch8AsyncSink<EventAnomaly> sink = Elasticsearch8AsyncSinkBuilder.<EventAnomaly>builder()
                .setHosts(HttpHost.create("http://elasticsearch:9200"))
                .setUsername("elastic")
                .setPassword("test")
                .setElementConverter(
                        (anomaly, ctx) -> new IndexOperation.Builder<EventAnomaly>()
                                .id(anomaly.getId().toString())
                                .document(anomaly)
                                .index("event-anomalies")
                                .build()
                ).build();

        anomalies.sinkTo(sink);

        env.execute("Anomaly Detection Job");
    }

}
