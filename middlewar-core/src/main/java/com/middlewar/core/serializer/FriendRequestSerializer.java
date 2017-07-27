package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.social.FriendRequest;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class FriendRequestSerializer extends JsonSerializer<FriendRequest> {

    @Override
    public void serialize(FriendRequest value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeObjectField("requester", value.getRequester());
        gen.writeObjectField("requested", value.getRequested());
        gen.writeStringField("message", value.getMessage());
        gen.writeNumberField("requestDate", value.getRequestDate());
        gen.writeEndObject();
    }
}
