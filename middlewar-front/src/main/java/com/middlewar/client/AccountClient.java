package com.middlewar.client;

import com.middlewar.dto.AccountDTO;

import java.util.HashMap;
import java.util.Map;

public class AccountClient extends APIClient {
    public static AccountDTO login(String username, String password) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("username", username);
        uriParams.put("password", password);
        AccountDTO account =  post("/login?username={username}&password={password}", AccountDTO.class, uriParams);

        if (account != null) {
            ClientConfig.TOKEN = account.getToken();
        }

        return account;
    }

    public static AccountDTO register(String username, String password) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("username", username);
        uriParams.put("password", password);

        AccountDTO account = post("/register?username={username}&password={password}", AccountDTO.class, uriParams);;

        if (account != null) {
            ClientConfig.TOKEN = account.getToken();
        }

        return account;
    }
}
