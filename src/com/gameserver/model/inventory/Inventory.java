package com.gameserver.model.inventory;

import org.springframework.data.annotation.Id;

/**
 * @author LEBOC Philippe
 */
public abstract class Inventory {

    @Id
    private String id;

    /**
     * @return the max inventory capacity
     */
    public abstract long getMaxWeight();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
