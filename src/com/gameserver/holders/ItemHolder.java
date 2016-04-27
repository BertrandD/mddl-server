package com.gameserver.holders;

import com.fasterxml.jackson.annotation.JsonView;
import com.util.data.json.View;

/**
 * @author LEBOC Philippe
 */
public class ItemHolder {

    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
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
