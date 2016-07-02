package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.social.FriendRequest;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class FriendRequestSerializer extends JsonSerializer<FriendRequest> {

    @Override
    public void serialize(FriendRequest value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeObjectField("requester", value.getRequester());
        gen.writeObjectField("requested", value.getRequested());
        gen.writeStringField("message", value.getMessage());
        gen.writeNumberField("requestDate", value.getRequestDate());
        gen.writeEndObject();
    }
}
