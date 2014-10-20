package fr.opensides.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.elasticsearch.ElasticSearchEventSerializer;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

//TODO ; gestion mauvais message
public class CustomElasticSearchSerializer implements ElasticSearchEventSerializer {


    @Override
    public XContentBuilder getContentBuilder(Event event) throws IOException {

        // This is the event's body
        String body = new String(event.getBody());

        String[] log = body.split("\\|");
        String[] headersToFill = log[0].split(";");

        XContentBuilder builder = null;
        try {
            builder = jsonBuilder().startObject();

            ContentBuilderUtil.appendField(builder, "@message", event.getBody());
            DateTime d = DateTime.parse(headersToFill[0]);
            long timestampMs = d.toDate().getTime();

            builder.field("@timestamp", new Date(timestampMs));


            builder.startObject("@fields");

            ContentBuilderUtil.appendField(builder, "id_noee", headersToFill[1].getBytes());
            ContentBuilderUtil.appendField(builder, "id_correction", headersToFill[2].getBytes());

            ContentBuilderUtil.appendField(builder, "hostname", headersToFill[3].getBytes());

            ContentBuilderUtil.appendField(builder, "application", headersToFill[4].getBytes());
            ContentBuilderUtil.appendField(builder, "app-specific", headersToFill[5].getBytes());
            ContentBuilderUtil.appendField(builder, "version", headersToFill[6].getBytes());
            ContentBuilderUtil.appendField(builder, "level", headersToFill[7].getBytes());
            ContentBuilderUtil.appendField(builder, "class", headersToFill[8].getBytes());
            ContentBuilderUtil.appendField(builder, "file", headersToFill[1].getBytes());

            ContentBuilderUtil.appendField(builder, "payload", log[1].getBytes());

            builder.endObject();


        } catch (IOException e) {
            e.printStackTrace();
        }
        // Let the enriched event go
        return builder;
    }

    @Override
    public void configure(Context context) {
        // NO-OP...
    }

    @Override
    public void configure(ComponentConfiguration conf) {
        // NO-OP...
    }
}