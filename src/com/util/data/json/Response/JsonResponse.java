package com.util.data.json.Response;

import java.util.HashMap;

/**
 * @author Bertrand
 */
public class JsonResponse {

    private String status;
    private Object payload;
    private final HashMap<String, Object> meta = new HashMap<>();

    public JsonResponse(){}

    public JsonResponse(JsonResponseType status){
        setStatus(status.getName());
        setPayload(null);
    }

    public JsonResponse(JsonResponseType status, String message) {
        setStatus(status.getName());
        setPayload(null);
        getMeta().put("message", message);
    }

    public JsonResponse(JsonResponseType status, MetaHolder... metadatas) {
        setStatus(status.getName());
        setPayload(null);
        for(MetaHolder holder : metadatas){
            addMetadata(holder.getKey(), holder.getObject());
        }
    }

    public JsonResponse(Object payload) {
        setStatus(JsonResponseType.SUCCESS.getName());
        setPayload(payload);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public HashMap<String, Object> getMeta() {
        return meta;
    }

    public void addMetadata(String key, Object value){
        getMeta().put(key, value);
    }
}
