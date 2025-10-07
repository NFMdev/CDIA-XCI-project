package com.github.NFMdev.cdia.processing_service.flink.job;

import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperationBuilders;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperationVariant;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.github.NFMdev.cdia.processing_service.flink.model.Event;
import com.github.NFMdev.cdia.processing_service.flink.model.EventAnomaly;
import com.github.NFMdev.cdia.processing_service.flink.function.AnomalyDetectionFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch8AsyncSink;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch8AsyncSinkBuilder;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.connector.jdbc.core.datastream.sink.JdbcSink;
import org.apache.flink.connector.jdbc.core.datastream.source.JdbcSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.http.HttpHost;
import java.time.Duration;
import java.util.Map;

public class AnomalyJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        JdbcSource<Event> jdbcSource = JdbcSource.<Event>builder()
                .setSql("SELECT id, description, location, source_id, created_at FROM events")
                .setDriverName("org.postgresql.Driver")
                .setDBUrl("jdbc:postgresql://postgres:5432/crime_analytics")
                .setUsername("admin")
                .setPassword("admin")
                .setTypeInformation(TypeInformation.of(Event.class))
                .setResultExtractor(rs -> new Event(
                        rs.getLong("id"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getLong("source_id"),
                        rs.getTimestamp("created_at")))
                .build();

        DataStream<Event> events = env.fromSource(
                jdbcSource,
                WatermarkStrategy.<Event>forMonotonousTimestamps()
                        .withTimestampAssigner((event, ts) -> event.getCreatedAt().toInstant().toEpochMilli()),
                "events"
        );

        DataStream<EventAnomaly> anomalies = events.keyBy(Event::getLocation)
                .window(TumblingEventTimeWindows.of(Duration.ofMinutes(1)))
                .process(new AnomalyDetectionFunction());

        anomalies.sinkTo(JdbcSink.<EventAnomaly>builder()
                .withQueryStatement(
                        "INSERT INTO event_anomalies(id, location, event_count, window_start, window_end, " +
                                "detected_at, rule, severity, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        (JdbcStatementBuilder<EventAnomaly>) (stmt, anomaly) -> {
                            stmt.setObject(1, anomaly.getId());
                            stmt.setString(2, anomaly.getLocation());
                            stmt.setLong(3, anomaly.eventCount);
                            stmt.setTimestamp(4, anomaly.getWindowStart());
                            stmt.setTimestamp(5, anomaly.getWindowEnd());
                            stmt.setTimestamp(6, anomaly.getDetectedAt());
                            stmt.setString(7, anomaly.getRule());
                            stmt.setString(8, anomaly.getSeverity());
                            stmt.setString(9, anomaly.getDescription());
                        }).buildAtLeastOnce(
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withUrl("jdbc:postgresql://postgres:5432/crime_analytics")
                                .withDriverName("org.postgresql.Driver")
                                .withUsername("admin")
                                .withPassword("admin")
                                .build()
                ));

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
