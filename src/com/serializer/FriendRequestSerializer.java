package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.FriendRequest;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class FriendRequestSerializer extends JsonSerializer<FriendRequest> {

    @Override
    public void serialize(FriendRequest value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeObjectFieldStart("requester");
        gen.writeStringField("id", value.getRequester().getId());
        gen.writeStringField("name", value.getRequester().getName());
        gen.writeEndObject();
        gen.writeObjectFieldStart("requested");
        gen.writeStringField("id", value.getRequested().getId());
        gen.writeStringField("name", value.getRequested().getName());
        gen.writeEndObject();
        gen.writeStringField("message", value.getMessage());
        gen.writeNumberField("requestDate", value.getRequestDate());
        gen.writeEndObject();
    }
}
