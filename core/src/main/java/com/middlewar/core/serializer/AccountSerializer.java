package com.middlewar.core.serializer;

import com.middlewar.core.model.Account;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class AccountSerializer extends JsonSerializer<Account> {
    @Override
    public void serialize(Account value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("username", value.getUsername());
        gen.writeStringField("token", value.getToken());
        gen.writeStringField("lang", value.getLang().name());
        gen.writeObjectField("authorities", value.getAuthorities());
        if(value.getCurrentPlayer() != null) gen.writeStringField("currentPlayer", value.getCurrentPlayer());
        gen.writeArrayFieldStart("players");
        for(String p : value.getPlayers()) gen.writeString(p);
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
