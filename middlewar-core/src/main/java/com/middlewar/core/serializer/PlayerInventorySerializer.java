package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.interfaces.IOConsumer;
import com.middlewar.core.model.inventory.PlayerInventory;

import java.io.IOException;

/**
 * @author Leboc Philippe.
 */
public class PlayerInventorySerializer extends JsonSerializer<PlayerInventory> {
    @Override
    public void serialize(PlayerInventory inventory, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("playerId", inventory.getPlayer().getId());

        gen.writeObjectFieldStart("items");
        IOConsumer.forEach(inventory.getItemsToMap().values().stream(), gen::writeObject);
        gen.writeEndObject();

        gen.writeEndObject();
    }
}
