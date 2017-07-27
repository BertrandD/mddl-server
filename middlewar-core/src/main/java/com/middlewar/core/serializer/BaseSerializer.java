package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.interfaces.IOConsumer;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.stats.Stats;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BaseSerializer extends JsonSerializer<Base> {
    @Override
    public void serialize(Base value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());

        gen.writeObjectFieldStart("owner");
        gen.writeNumberField("id", value.getOwner().getId());
        gen.writeStringField("name", value.getOwner().getName());
        gen.writeEndObject();

        gen.writeObjectFieldStart("resources");
        IOConsumer.forEach(value.getResources().stream(), resource -> {
            gen.writeNumber(resource.getId());
            gen.writeStringField("templateId", resource.getItem().getTemplateId());
            gen.writeNumberField("count", resource.getCount());
            gen.writeNumberField("maxVolume", value.getBaseStat().getValue(Stats.valueOf("MAX_" + resource.getItem().getTemplateId().toUpperCase()))); // TODO: WARNING
            gen.writeNumberField("production", resource.getProdPerHour());
            gen.writeNumberField("lastRefresh", resource.getLastRefresh());
            gen.writeEndObject();
        });
        gen.writeEndObject();

        gen.writeObjectField("baseStat", value.getBaseStat());
        gen.writeObjectField("buildings", value.getBuildings());
        gen.writeObjectField("inventory", value.getBaseInventory());
        gen.writeObjectField("ships", value.getShips());
        gen.writeObjectField("planet", value.getPlanet());
        gen.writeEndObject();
    }
}
