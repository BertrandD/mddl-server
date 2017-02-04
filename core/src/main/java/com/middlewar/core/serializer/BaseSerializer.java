package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.interfaces.IOConsumer;
import com.middlewar.core.model.Base;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BaseSerializer extends JsonSerializer<Base> {
    @Override
    public void serialize(Base value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("name", value.getName());

        gen.writeObjectFieldStart("owner");
        gen.writeStringField("id", value.getOwner().getId());
        gen.writeStringField("name", value.getOwner().getName());
        gen.writeEndObject();

        gen.writeObjectFieldStart("resources");
        IOConsumer.forEach(value.getResources().stream(), itemContainer -> {
            gen.writeObjectFieldStart(itemContainer.getItem().getId());
            gen.writeStringField("templateId", itemContainer.getItem().getTemplateId());
            gen.writeNumberField("count", itemContainer.getItem().getCount());
            gen.writeNumberField("maxVolume", itemContainer.getMaxVolume());
            gen.writeNumberField("production", value.getBaseStat().getValue(itemContainer.getStat()));
            gen.writeNumberField("lastRefresh", itemContainer.getLastRefresh());
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
