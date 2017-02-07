package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.serializer.ResourceInventorySerializer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "resource_inventory")
@JsonSerialize(using = ResourceInventorySerializer.class)
public final class ResourceInventory extends Inventory {

    @DBRef
    @JsonBackReference
    private Base base;

    private long lastRefresh;

    public ResourceInventory(Base base) {
        setId(new ObjectId().toString());
        setBase(base);
        setLastRefresh(System.currentTimeMillis());
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
