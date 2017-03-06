package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.projections.AstralObjectLight;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class AstralObjectSerializer extends JsonSerializer<AstralObject> {
    @Override
    public void serialize(AstralObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeNumberField("orbit", value.getOrbit());
        gen.writeNumberField("revolution", value.getRevolution());
        gen.writeNumberField("angle", value.getAngle());
        gen.writeObjectField("parent", new AstralObjectLight(value.getParent()));

        gen.writeArrayFieldStart("satellites");
        for (AstralObject satellite: value.getSatellites()) {
            gen.writeObject(satellite);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
