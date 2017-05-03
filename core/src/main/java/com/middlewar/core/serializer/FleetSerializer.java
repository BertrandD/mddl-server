package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.interfaces.IOConsumer;
import com.middlewar.core.model.vehicles.Fleet;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class FleetSerializer extends JsonSerializer<Fleet> {
    @Override
    public void serialize(Fleet value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("departure", value.getDeparture().toString());
        gen.writeStringField("arrival", value.getArrival().toString());
        gen.writeStringField("mission", value.getMission().name());
        gen.writeObjectField("stats", value.getStats());

        gen.writeArrayFieldStart("ships");
        IOConsumer.forEach(value.getShips().stream(), ship -> {
            gen.writeStringField("id", ship.getId());
            gen.writeNumberField("count", ship.getCount());
        });
        gen.writeStartObject();

        gen.writeEndObject();
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
