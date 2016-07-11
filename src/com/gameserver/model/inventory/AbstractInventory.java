package com.gameserver.model.inventory;

import com.gameserver.interfaces.IInventory;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * @author LEBOC Philippe
 */
public abstract class AbstractInventory implements IInventory {

    @Id
    protected String id;

    protected AbstractInventory() {
        setId(new ObjectId().toString());
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }
}
