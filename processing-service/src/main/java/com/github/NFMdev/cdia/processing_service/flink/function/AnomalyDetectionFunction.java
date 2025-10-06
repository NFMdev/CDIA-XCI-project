package com.github.NFMdev.cdia.processing_service.flink.function;

import com.github.NFMdev.cdia.processing_service.flink.data.Event;
import com.github.NFMdev.cdia.processing_service.flink.data.EventAnomaly;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Instant;

public class AnomalyDetectionFunction extends ProcessWindowFunction<Event, EventAnomaly, String, TimeWindow> {
    @Override
    public void process(String location, Context context, Iterable<Event> events, Collector<EventAnomaly> out) {
        long count = 0;
        for (Event e : events) count++;

        if (count > 10) {
            out.collect(new EventAnomaly(
                    location,
                    count,
                    new Timestamp(context.window().getStart()),
                    new Timestamp(context.window().getEnd()),
                    Timestamp.from(Instant.now()),
                    ">10 events in 1 min",
                    "HIGH",
                    "More than 10 events detected in " + location + " within 1 minute."
            ));
        }
    }
}
