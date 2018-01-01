package com.middlewar.core.holders;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public class ItemHolder {

    @Id
    private String id;
    private long count;

    public ItemHolder(String id, long count) {
        setId(id);
        setCount(count);
    }
}
