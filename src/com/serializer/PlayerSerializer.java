package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.Base;
import com.gameserver.model.Player;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class PlayerSerializer extends JsonSerializer<Player> {
    @Override
    public void serialize(Player value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("currentBase", value.getCurrentBase().getId());
        gen.writeArrayFieldStart("bases");
        for(Base base : value.getBases())
            gen.writeString(base.getId());
        gen.writeEndArray();
        gen.writeObjectField("inventory", value.getInventory().getItems());
        gen.writeEndObject();
    }
}
