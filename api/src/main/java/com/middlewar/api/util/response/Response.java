package com.middlewar.api.util.response;

import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.Lang;

import java.util.HashMap;

/**
 * @author Bertrand
 */
public class Response<T> {

    private String status;
    private T payload;
    private final HashMap<String, Object> meta = new HashMap<>();

    public Response(){}

    public Response(JsonResponseType status){
        setStatus(status.getName());
        setPayload(null);
    }

    public Response(Lang lang, String messageId){
        setStatus(JsonResponseType.ERROR.getName());
        setPayload(null);
        getMeta().put("message", SystemMessageData.getInstance().getMessage(lang, messageId));
    }

    public Response(JsonResponseType type, Lang lang, String messageId){
        setStatus(type.getName());
        setPayload(null);
        getMeta().put("message", SystemMessageData.getInstance().getMessage(lang, messageId));
    }

    public Response(JsonResponseType status, String message) {
        setStatus(status.getName());
        setPayload(null);
        getMeta().put("message", message);
    }

    public Response(JsonResponseType status, MetaHolder... metas) {
        setStatus(status.getName());
        setPayload(null);
        for(MetaHolder holder : metas){
            addMeta(holder.getKey(), holder.getObject());
        }
    }

    public Response(T payload) {
        setStatus(JsonResponseType.SUCCESS.getName());
        setPayload(payload);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public HashMap<String, Object> getMeta() {
        return meta;
    }

    public void addMeta(String key, Object value){
        getMeta().put(key, value);
    }
}
