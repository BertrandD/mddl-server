package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.Base;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BaseSerializer extends JsonSerializer<Base> {
    @Override
    public void serialize(Base value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());

        gen.writeObjectFieldStart("owner");
        gen.writeStringField("id", value.getOwner().getId());
        gen.writeStringField("name", value.getOwner().getName());
        gen.writeEndObject();

        gen.writeObjectField("baseStat", value.getBaseStat());
        gen.writeNumberField("energy", value.getEnergy());

        if(value.getProduction() != null)
            gen.writeObjectField("production", value.getProduction());

        gen.writeObjectField("buildings", value.getBuildings());
        gen.writeObjectField("inventory", value.getBaseInventory());
        gen.writeEndObject();
    }
}
