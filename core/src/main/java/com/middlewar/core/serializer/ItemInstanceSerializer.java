package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.instances.ItemInstance;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class ItemInstanceSerializer extends JsonSerializer<ItemInstance> {

    @Override
    public void serialize(ItemInstance value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("templateId", value.getTemplateId());
        gen.writeNumberField("count", (long)Math.floor(value.getCount()));
        gen.writeStringField("type", value.getType().name());
        gen.writeEndObject();
    }
}
