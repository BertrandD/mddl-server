package com.util.data.json.Response;

import java.util.HashMap;

/**
 * @author Bertrand
 */
public class JsonResponse {

    private String status;
    private Object payload;
    private HashMap<String, Object> metadata;

    public JsonResponse(JsonResponseType status){
        setStatus(status.getName());
        setPayload(null);
        setMetadata(null);
    }

    public JsonResponse(JsonResponseType status, String message) {
        setStatus(status.getName());
        setPayload(null);
        setMetadata(new HashMap<>());
        getMetadata().put("message", message);
    }

    public JsonResponse(JsonResponseType status, HashMap<String, Object> metadata) {
        setStatus(status.getName());
        setPayload(null);
        setMetadata(metadata);
    }

    public JsonResponse(Object payload) {
        setStatus(JsonResponseType.SUCCESS.getName());
        setPayload(payload);
        setMetadata(null);
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public Object getPayload() {
        return payload;
    }

    private void setPayload(Object payload) {
        this.payload = payload;
    }

    public HashMap<String, Object> getMetadata() {
        return metadata;
    }

    private void setMetadata(HashMap<String, Object> metadata) {
        this.metadata = metadata;
    }


}
