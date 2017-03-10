package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.interfaces.IOConsumer;
import com.middlewar.core.model.inventory.BaseInventory;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BaseInventorySerializer extends JsonSerializer<BaseInventory> {
    @Override
    public void serialize(BaseInventory value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        IOConsumer.forEach(value.getItemsToMap().values().stream(), item -> gen.writeObjectField(item.getTemplateId(), item));
        gen.writeEndObject();
    }
}
