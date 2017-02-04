package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public void serialize(Player value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {

        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("lang", value.getAccount().getLang().name());

        if(value.getCurrentBase() != null) gen.writeStringField("currentBase", value.getCurrentBase().getId());

        gen.writeArrayFieldStart("friends");
        IOConsumer.forEach(value.getFriends().stream(), gen::writeObject);
        gen.writeEndArray();

        gen.writeArrayFieldStart("friendRequests");
        IOConsumer.forEach(value.getFriendRequests().stream(), gen::writeObject);
        gen.writeEndArray();

        gen.writeArrayFieldStart("bases");
        IOConsumer.forEach(value.getBases().stream(), base -> gen.writeString(base.getId()));
        gen.writeEndArray();

        //gen.writeObjectField("inventory", value.getInventory().getItems()); // TODO: re-implement me
        gen.writeObjectField("planetScans", value.getPlanetScans());
        gen.writeObjectField("inventory", value.getInventory().getItems());
        gen.writeEndObject();
    }
}
