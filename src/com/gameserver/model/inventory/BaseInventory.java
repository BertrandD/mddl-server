package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.model.Base;
import com.serializer.BaseInventorySerializer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "base_inventory")
@JsonSerialize(using = BaseInventorySerializer.class)
public final class BaseInventory extends Inventory {

    @DBRef
    @JsonBackReference
    private Base base;

    public BaseInventory() {
        super();
    }

    public BaseInventory(Base base){
        super();
        setId(new ObjectId().toString());
        setBase(base);
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }
}
