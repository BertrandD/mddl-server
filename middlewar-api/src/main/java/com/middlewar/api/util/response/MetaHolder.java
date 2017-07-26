package com.middlewar.api.util.response;

/**
 * @author LEBOC Philippe
 */
public class MetaHolder {

    private String key;
    private Object object;

    public MetaHolder(String key, Object o){
        setKey(key);
        setObject(o);
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public Object getObject() {
        return object;
    }

    private void setObject(Object object) {
        this.object = object;
    }
}
