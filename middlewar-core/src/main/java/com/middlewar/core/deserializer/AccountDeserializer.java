package com.middlewar.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;

import java.io.IOException;

/**
 * @author Bertrand
 */
public class AccountDeserializer extends JsonDeserializer<Account> {
    @Override
    public Account deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Account account = new Account();
        int id = (Integer) node.get("id").numberValue();
        String username = node.get("username").asText();
        String token = node.get("token").asText();
        Lang lang = Lang.valueOf(node.get("lang").asText());

        JsonNode playersNode = node.get("players");

        account.setId(id);
        account.setUsername(username);
        account.setToken(token);
        account.setLang(lang);
        return account;
    }
}
