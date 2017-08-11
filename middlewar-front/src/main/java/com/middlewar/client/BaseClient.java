package com.middlewar.client;

import com.middlewar.dto.BaseDTO;

import java.util.HashMap;
import java.util.Map;

public class BaseClient extends APIClient {
    public static BaseDTO createBase(String name) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("name", name);

        return call("/me/base?name={name}", BaseDTO.class, uriParams);
    }
}
