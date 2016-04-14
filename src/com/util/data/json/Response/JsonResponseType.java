package com.util.data.json.Response;

/**
 * @author LEBOC Philippe
 */
public enum JsonResponseType {
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
