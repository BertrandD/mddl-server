package com.middlewar.api.util.response;

/**
 * @author LEBOC Philippe
 */
public enum JsonResponseType {
    UNAUTHORIZED("unauthorized"),
    ERROR("error"),
    SUCCESS("ok");

    private String name;

    JsonResponseType(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
