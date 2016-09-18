package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.Base;
import com.gameserver.model.inventory.ResourceInventory;

import java.io.IOException;
import java.util.stream.Collectors;

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
        for (ResourceInventory inventory : value.getResources()) {
            gen.writeObjectFieldStart(inventory.getItem().getId());
            gen.writeStringField("templateId", inventory.getItem().getTemplateId());
            gen.writeNumberField("count", inventory.getItem().getCount());
            gen.writeNumberField("maxVolume", inventory.getMaxVolume());
            gen.writeNumberField("production", value.getBaseStat().getValue(inventory.getStat()));
            gen.writeNumberField("lastRefresh", inventory.getLastRefresh());
            gen.writeEndObject();
        }
        gen.writeEndObject();

        gen.writeObjectField("baseStat", value.getBaseStat());

        gen.writeObjectField("buildings", value.getBuildings().stream().filter(k -> k.getEndsAt() == -1).collect(Collectors.toList()));
        gen.writeObjectField("inventory", value.getBaseInventory());

        gen.writeEndObject();
    }
}
