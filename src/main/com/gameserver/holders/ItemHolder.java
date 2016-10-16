package com.gameserver.holders;

/**
 * @author LEBOC Philippe
 */
public class ItemHolder {

    private String id;
    private long count;

    public ItemHolder(String id, long count){
        setId(id);
        setCount(count);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
