package com.util.data.json.Response;

import com.fasterxml.jackson.annotation.JsonView;
import com.util.data.json.View;

import java.util.HashMap;

/**
 * @author Bertrand
 */
public class JsonResponse {

    @JsonView(View.Standard.class)
    private String status;

    @JsonView(View.Standard.class)
    private Object payload;

    @JsonView(View.Standard.class)
    private final HashMap<String, Object> metadata = new HashMap<>();

    public JsonResponse(){}

    public JsonResponse(JsonResponseType status){
        setStatus(status.getName());
        setPayload(null);
    }

    public JsonResponse(JsonResponseType status, String message) {
        setStatus(status.getName());
        setPayload(null);
        getMetadata().put("message", message);
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

    public HashMap<String, Object> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, Object value){
        getMetadata().put(key, value);
    }
}
