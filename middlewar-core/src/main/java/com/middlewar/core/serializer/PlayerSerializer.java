package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.interfaces.IOConsumer;
import com.middlewar.core.model.Player;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class PlayerSerializer extends JsonSerializer<Player> {
    @Override
    public void serialize(Player value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("lang", value.getAccount().getLang().name());

        if (value.getCurrentBase() != null) gen.writeNumberField("currentBase", value.getCurrentBase().getId());

        gen.writeArrayFieldStart("friends");
        IOConsumer.forEach(value.getFriends().stream(), gen::writeObject);
        gen.writeEndArray();

        gen.writeArrayFieldStart("friendRequests");
        IOConsumer.forEach(value.getReceivedFriendRequests().stream(), gen::writeObject);
        gen.writeEndArray();

        gen.writeArrayFieldStart("emittedFriendRequests");
        IOConsumer.forEach(value.getEmittedFriendRequests().stream(), gen::writeObject);
        gen.writeEndArray();

        gen.writeArrayFieldStart("bases");
        IOConsumer.forEach(value.getBases().stream(), base -> gen.writeNumber(base.getId()));
        gen.writeEndArray();

        gen.writeObjectField("inventory", value.getInventory().getItems());
        gen.writeObjectField("planetScans", value.getPlanetScans());
        gen.writeObjectField("inventory", value.getInventory().getItems());
        gen.writeEndObject();
    }
}
