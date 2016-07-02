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
        gen.writeStringField("baseId", value.getBase().getId());
        gen.writeNumberField("lastRefresh", value.getLastRefresh());

        gen.writeObjectFieldStart("items");
        for(ItemInstance item : value.getItems()){
            gen.writeObjectField(item.getTemplateId(), item);
        }
        gen.writeEndObject();

        /**
        *
        * DETAILLED INFORMATION
        *
        gen.writeArrayFieldStart("resource");
        for(ItemInstance item : value.getItems().stream().filter(ItemInstance::isResource).collect(Collectors.toList()))
            gen.writeObject(item);
        gen.writeEndArray();

        gen.writeArrayFieldStart("cargo");
        for(ItemInstance item : value.getItems().stream().filter(ItemInstance::isCargo).collect(Collectors.toList()))
            gen.writeObject(item);
        gen.writeEndArray();

        gen.writeArrayFieldStart("engine");
        for(ItemInstance item : value.getItems().stream().filter(ItemInstance::isEngine).collect(Collectors.toList()))
            gen.writeObject(item);
        gen.writeEndArray();

        gen.writeArrayFieldStart("module");
        for(ItemInstance item : value.getItems().stream().filter(ItemInstance::isModule).collect(Collectors.toList()))
            gen.writeObject(item);
        gen.writeEndArray();

        gen.writeArrayFieldStart("structure");
        for(ItemInstance item : value.getItems().stream().filter(ItemInstance::isStructure).collect(Collectors.toList()))
            gen.writeObject(item);
        gen.writeEndArray();

        gen.writeArrayFieldStart("weapon");
        for(ItemInstance item : value.getItems().stream().filter(ItemInstance::isWeapon).collect(Collectors.toList()))
            gen.writeObject(item);
        gen.writeEndArray();
        */

        gen.writeEndObject();
    }
}
