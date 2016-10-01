package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BaseInventorySerializer extends JsonSerializer<BaseInventory> {
    @Override
    public void serialize(BaseInventory value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        for(ItemInstance item : value.getItems()){
            gen.writeObjectField(item.getTemplateId(), item);
        }

        gen.writeEndObject();
    }
}
