package com.github.NFMdev.cdia.processing_service.flink.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.NFMdev.cdia.processing_service.flink.model.Event;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.cdc.connectors.shaded.org.apache.kafka.connect.data.Struct;
import org.apache.flink.cdc.connectors.shaded.org.apache.kafka.connect.source.SourceRecord;
import org.apache.flink.cdc.debezium.DebeziumDeserializationSchema;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Instant;

public class EventDebeziumDeserializationSchema implements DebeziumDeserializationSchema<Event> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<Event> collector) throws Exception {
        Struct valueStruct = (Struct) sourceRecord.value();
        if (valueStruct == null) return;

        // The "after" field contains the row after insert/update
        Struct after = (Struct) valueStruct.get("after");
        if (after != null) {
            Event event = new Event(
                    after.getInt64("id"),
                    after.getString("description"),
                    after.getString("location"),
                    after.getInt64("source_id"),
                    new Timestamp(after.getInt64("created_at"))
            );
            collector.collect(event);
        }
    }

    @Override
    public TypeInformation<Event> getProducedType() {
        return null;
    }
}
