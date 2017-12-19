package com.middlewar.api.util.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bertrand
 */
@Getter
@NoArgsConstructor
public class Response {

    private Map<String, Object> metadata;

    private Object payload;

    public Response(Object payload) {
        this.payload = payload;
        this.metadata = new HashMap<>();
    }
}
