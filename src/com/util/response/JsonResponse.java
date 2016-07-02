package com.util.response;

import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.Lang;

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

    public JsonResponse(Lang lang, String messageId){
        setStatus(JsonResponseType.ERROR.getName());
        setPayload(null);
        getMeta().put("message", SystemMessageData.getInstance().getMessage(lang, messageId));
    }

    public JsonResponse(JsonResponseType type, Lang lang, String messageId){
        setStatus(type.getName());
        setPayload(null);
        getMeta().put("message", SystemMessageData.getInstance().getMessage(lang, messageId));
    }

    public JsonResponse(JsonResponseType status, String message) {
        setStatus(status.getName());
        setPayload(null);
        getMeta().put("message", message);
    }

    public JsonResponse(JsonResponseType status, MetaHolder... metas) {
        setStatus(status.getName());
        setPayload(null);
        for(MetaHolder holder : metas){
            addMeta(holder.getKey(), holder.getObject());
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

    public void addMeta(String key, Object value){
        getMeta().put(key, value);
    }
}
