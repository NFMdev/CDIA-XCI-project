package com.github.NFMdev.cdia.processing_service.flink;

import com.github.NFMdev.cdia.processing_service.flink.data.EventAggregate;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.connector.jdbc.core.datastream.sink.JdbcSink;
import org.apache.flink.connector.jdbc.core.datastream.source.JdbcSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class FlinkEventJob {

    public static void main(String[] args) throws Exception {
        // Use regular environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // JDBC source
        JdbcSource<Event> jdbcSource = JdbcSource.<Event>builder()
                .setSql("SELECT id, description, location, source_id FROM events")
                .setDriverName("org.postgresql.Driver")
                .setDBUrl("jdbc:postgresql://postgres:5432/crime_analytics")
                .setUsername("admin")
                .setPassword("admin")
                .setTypeInformation(TypeInformation.of(Event.class))
                .setResultExtractor(rs -> new Event(
                        rs.getLong("id"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getLong("source_id")))
                .build();

        DataStream<Event> stream = env.fromSource(
                jdbcSource,
                org.apache.flink.api.common.eventtime.WatermarkStrategy.noWatermarks(),
                "events"
        );

        DataStream<EventAggregate> aggregates = stream
                .map(event -> new EventAggregate(event.location, event.description, 1L))
                .keyBy(event -> event.location)
                .reduce((e1, e2) -> new EventAggregate(e1.location, e1.eventType, e1.count + e2.count))
                .map(eventAgg -> new EventAggregate(eventAgg.location, eventAgg.eventType, eventAgg.count));

        JdbcSink<EventAggregate> sink = JdbcSink.<EventAggregate>builder()
                .withQueryStatement(
                        "INSERT INTO event_aggregates(location, event_type, count, window_start, window_end) " +
                                "VALUES (?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET count = EXCLUDED.count",
                        new JdbcStatementBuilder<EventAggregate>() {
                            @Override
                            public void accept(PreparedStatement stmt, EventAggregate agg) throws SQLException {
                                stmt.setString(1, agg.location);
                                stmt.setString(2, agg.eventType);
                                stmt.setLong(3, agg.count);
                                stmt.setTimestamp(4, Timestamp.from(Instant.now()));
                                stmt.setTimestamp(5, Timestamp.from(Instant.now()));
                            }
                        }).buildAtLeastOnce(
                                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                        .withUrl("jdbc:postgresql://postgres:5432/crime_analytics")
                                        .withDriverName("org.postgresql.Driver")
                                        .withUsername("admin")
                                        .withPassword("admin")
                                        .build()
                );

        aggregates.sinkTo(sink);

        env.execute("Flink Event Job");
    }

    static class Event {
        public Long id;
        public String description;
        public String location;
        public Long sourceId;

        public Event() {}

        public Event(Long id, String description, String location, Long sourceId) {
            this.id = id;
            this.description = description;
            this.location = location;
            this.sourceId = sourceId;
        }
    }
}