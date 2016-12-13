package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.social.FriendRequest;
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

        if(value.getCurrentBase() != null)
            gen.writeStringField("currentBase", value.getCurrentBase().getId());

        gen.writeArrayFieldStart("friends");
        for(PlayerHolder friend : value.getFriends()) {
            gen.writeObject(friend);
        }
        gen.writeEndArray();

        gen.writeArrayFieldStart("friendRequests");
        for(FriendRequest request : value.getFriendRequests()) {
            gen.writeObject(request);
        }
        gen.writeEndArray();

        gen.writeArrayFieldStart("bases");
        for(Base base : value.getBases())
            gen.writeString(base.getId());
        gen.writeEndArray();

        gen.writeObjectField("inventory", value.getInventory().getItems());
        gen.writeEndObject();
    }
}
