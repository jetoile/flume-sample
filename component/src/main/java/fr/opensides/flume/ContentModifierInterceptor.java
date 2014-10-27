package fr.opensides.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * User: khanh
 * To change this template use File | Settings | File Templates.
 */
public class ContentModifierInterceptor implements Interceptor {


    public ContentModifierInterceptor() {
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

        //TODO : hack pour le timestamp : on le recupere du header et on le force dans le body
        //TODO : on considere que le timestamp est toujours le premier champ
        String timestamp = event.getHeaders().get("timestamp");
        if (timestamp == null) {
            System.out.println("log non standard: unable to parse: " + body);
        } else {
            int commaIndex = newBody.indexOf(";");

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.valueOf(timestamp));

            newBody = cal.getTime() + newBody.substring(commaIndex);
        }

        event.setBody(newBody.getBytes());

        System.out.println(event.getHeaders());

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
