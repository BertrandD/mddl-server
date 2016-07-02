package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.holders.StatHolder;
import com.gameserver.model.commons.BaseStat;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BaseStatSerializer extends JsonSerializer<BaseStat> {
    @Override
    public void serialize(BaseStat value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        for(StatHolder holder : value.getStats().values()) {
            gen.writeNumberField(holder.getStat().toString(), holder.getValue());
        }
        gen.writeEndObject();
    }
}
