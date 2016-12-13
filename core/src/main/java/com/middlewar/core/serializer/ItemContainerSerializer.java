package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.inventory.ItemContainer;

import java.io.IOException;

/**
 * @author Leboc Philippe.
 */
public class ItemContainerSerializer extends JsonSerializer<ItemContainer> {
    @Override
    public void serialize(ItemContainer value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        gen.writeStringField("id", value.getId());
        gen.writeStringField("templateId", value.getItem().getTemplateId());
        gen.writeNumberField("count", (long)Math.floor(value.getItem().getCount()));
        gen.writeStringField("type", value.getItem().getType().name());
        gen.writeNumberField("lastRefresh", value.getLastRefresh());

        gen.writeEndObject();
    }
}
