package com.middlewar.core.holders;

import lombok.Data;

import javax.persistence.Id;

/**
 * @author LEBOC Philippe
 */
@Data
public class ItemHolder {

    @Id
    private String id;
    private long count;

    public ItemHolder(String id, long count) {
        setId(id);
        setCount(count);
    }
}