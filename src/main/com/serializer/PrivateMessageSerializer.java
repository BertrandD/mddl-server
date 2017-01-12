package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.social.PrivateMessage;

import java.io.IOException;

/**
 * @author DARBON Bertrand
 */
public class PrivateMessageSerializer extends JsonSerializer<PrivateMessage> {
    @Override
    public void serialize(PrivateMessage value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("message", value.getMessage());
        gen.writeNumberField("date", value.getDate());
        gen.writeBooleanField("isRead", value.isRead());
        gen.writeObjectField("author", value.getAuthor());
        gen.writeEndObject();
        System.out.println(gen.toString());
    }
}
