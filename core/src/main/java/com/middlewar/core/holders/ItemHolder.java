package com.middlewar.core.holders;

import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
public class ItemHolder {

    private String id;
    private long count;

    public ItemHolder(String id, long count){
        setId(id);
        setCount(count);
    }
}
