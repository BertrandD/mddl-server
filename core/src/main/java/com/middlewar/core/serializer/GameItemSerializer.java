package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.items.Item;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class GameItemSerializer extends JsonSerializer<GameItem> {
    @Override
    public void serialize(GameItem value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("itemId", value.getItemId());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeStringField("type", value.getType().name().toLowerCase());
        gen.writeNumberField("weight", value.getWeight());
        gen.writeNumberField("volume", value.getVolume());

        if(value instanceof Cargo)
            gen.writeNumberField("capacity", ((Cargo) value).getCapacity());

        if(value instanceof Engine)
            gen.writeNumberField("power", ((Engine) value).getPower());

        if(value instanceof Structure)
            gen.writeObjectField("slots", ((Structure) value).getAvailablesSlots());

        if(value instanceof Weapon)
            gen.writeNumberField("damage", ((Weapon) value).getDamage());

        gen.writeObjectFieldStart("stats");
        for (StatHolder holder : value.getAllStats()) {
            gen.writeNumberField(holder.getStat().name(), holder.getValue());
        }
        gen.writeEndObject();

        if(value instanceof Item)
        {
            gen.writeStringField("rank", ((Item) value).getRank().getName());
            gen.writeNumberField("buildTime", ((Item) value).getBuildTime());
            gen.writeObjectField("requirement", ((Item) value).getRequirement());
        }

        gen.writeEndObject();
    }
}
