package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.stats.ObjectStat;

import java.io.IOException;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
public class ObjectStatSerializer extends JsonSerializer<ObjectStat> {
    @Override
    public void serialize(ObjectStat value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        for (Map.Entry<Stats, Double> entry : value.getStats().entrySet()) {
            switch(entry.getKey())
            {
                case RESOURCE_3:
                case RESOURCE_2:
                case RESOURCE_4:
                case RESOURCE_1:
                case RESOURCE_5:
                    break;
                default: gen.writeNumberField(entry.getKey().toString(), entry.getValue());
            }
        }
        gen.writeEndObject();
    }
}
