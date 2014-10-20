package fr.opensides.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.interceptor.Interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: khanh
 * To change this template use File | Settings | File Templates.
 */
public class ContentModifierInterceptor implements Interceptor {


    public ContentModifierInterceptor(){
    }

    @Override
    public void initialize() {
        // At interceptor start up
    }

    @Override
    public Event intercept(Event event) {

        // This is the event's body
        String body = new String(event.getBody());

        String newBody = body.replaceAll("\\|", ";");
        event.setBody(newBody.getBytes());

        System.out.println("======== intercept: " + new String(event.getBody()));

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

        @Override
        public void configure(Context context) {
            // Retrieve property from flume conf
        }

        @Override
        public Interceptor build() {
            return new ContentModifierInterceptor();
        }
    }
}
