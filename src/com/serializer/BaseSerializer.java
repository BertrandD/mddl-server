package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.Base;
import com.gameserver.model.inventory.ResourceInventory;

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

        gen.writeArrayFieldStart("resources");
        for (ResourceInventory inventory : value.getResources()) {
            gen.writeStartObject();
            gen.writeStringField("id", inventory.getItem().getId());
            gen.writeStringField("templateId", inventory.getItem().getTemplateId());
            gen.writeNumberField("count", inventory.getItem().getCount());
            gen.writeNumberField("maxVolume", inventory.getMaxVolume());
            gen.writeNumberField("production", value.getBaseStat().getValue(inventory.getStat()));
            gen.writeNumberField("lastRefresh", inventory.getLastRefresh());
            gen.writeEndObject();
        }
        gen.writeEndArray();

        gen.writeObjectField("baseStat", value.getBaseStat());

        gen.writeObjectField("buildings", value.getBuildings());
        gen.writeObjectField("inventory", value.getBaseInventory());

        gen.writeEndObject();
    }
}
