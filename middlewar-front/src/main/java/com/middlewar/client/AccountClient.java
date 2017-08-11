package com.middlewar.client;

import com.middlewar.dto.AccountDTO;

import java.util.HashMap;
import java.util.Map;

public class AccountClient extends APIClient {
    public static AccountDTO login(String username, String password) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("username", username);
        uriParams.put("password", password);

        return call("/login?username={username}&password={password}", AccountDTO.class, uriParams);
    }

    public static AccountDTO register(String username, String password) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("username", username);
        uriParams.put("password", password);

        return call("/register?username={username}&password={password}", AccountDTO.class, uriParams);
    }
}
