package com.github.NFMdev.cdia.processing_service.flink.job;

import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.github.NFMdev.cdia.processing_service.flink.deserializer.EventDebeziumDeserializationSchema;
import com.github.NFMdev.cdia.processing_service.flink.model.Event;
import com.github.NFMdev.cdia.processing_service.flink.model.EventAnomaly;
import com.github.NFMdev.cdia.processing_service.flink.function.AnomalyDetectionFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cdc.connectors.base.source.jdbc.JdbcIncrementalSource;
import org.apache.flink.cdc.connectors.postgres.source.PostgresSourceBuilder;
import org.apache.flink.cdc.debezium.DebeziumDeserializationSchema;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch8AsyncSink;
import org.apache.flink.connector.elasticsearch.sink.Elasticsearch8AsyncSinkBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.http.HttpHost;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

public class AnomalyJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // Custom deserializer for CDC events
        DebeziumDeserializationSchema<Event> deserializer = new EventDebeziumDeserializationSchema();

        // CDC source
        JdbcIncrementalSource<Event> postgresSource = PostgresSourceBuilder.PostgresIncrementalSource.<Event>builder()
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

        // CDC stream from Postgres, uses created_at as watermark
        DataStream<Event> cdcEventsStream = env.fromSource(
                postgresSource,
                WatermarkStrategy.<Event>forMonotonousTimestamps()
                        .withTimestampAssigner((event, l) -> event.getCreatedAt().getTime()),
                "PostgresCDC"
        ).returns(Event.class);


        // Anomaly detection and parser stream with a 1-minute window
        DataStream<EventAnomaly> anomalies = cdcEventsStream
                .keyBy(Event::getLocation)
                .window(TumblingEventTimeWindows.of(Duration.ofMinutes(1)))
                .process(new AnomalyDetectionFunction());


        // ES sink
        Elasticsearch8AsyncSink<EventAnomaly> sink = Elasticsearch8AsyncSinkBuilder.<EventAnomaly>builder()
                .setHosts(HttpHost.create("http://cdia-elasticsearch:9200"))
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
