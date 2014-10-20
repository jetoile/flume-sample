package fr.opensides.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: khanh
 * To change this template use File | Settings | File Templates.
 */
public class HeaderExtractorInterceptor implements Interceptor {


    public HeaderExtractorInterceptor(){
    }

    @Override
    public void initialize() {
        // At interceptor start up
    }

    @Override
    public Event intercept(Event event) {

        // This is the event's body
        String body = new String(event.getBody());

        String[] log = body.split("\\|");
        String[] headersToFill = log[0].split(";");

        // These are the event's headers
        Map<String, String> headers = event.getHeaders();

        DateTime d = DateTime.parse(headersToFill[0]);
        long timestampMs = d.toDate().getTime();
        headers.put("timestamp", String.valueOf(timestampMs));


        headers.put("id_noee", headersToFill[1]);
        headers.put("id_correction", headersToFill[2]);

        headers.put("hostname", headersToFill[3]);

        headers.put("application", headersToFill[4]);
        headers.put("app-specific", headersToFill[5]);
        headers.put("version", headersToFill[6]);
        headers.put("level", headersToFill[7]);
        headers.put("class", headersToFill[8]);
        headers.put("file", headersToFill[9]);
        headers.put("payload", log[1]);

        System.out.println("======== headerExtractor: " + headers.toString());

        // Let the enriched event go
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {

        List<Event> interceptedEvents =
                new ArrayList<Event>(events.size());
        for (Event event : events) {
            // Intercept any event
            Event interceptedEvent = intercept(event);
            interceptedEvents.add(interceptedEvent);
        }

        return interceptedEvents;
    }

    @Override
    public void close() {
        // At interceptor shutdown
    }

    public static class Builder
            implements Interceptor.Builder {

        private String hostHeader;

        @Override
        public void configure(Context context) {
            // Retrieve property from flume conf
        }

        @Override
        public Interceptor build() {
            return new HeaderExtractorInterceptor();
        }
    }
}
