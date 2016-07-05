package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.model.stats.BaseStat;
import com.gameserver.model.Base;
import com.serializer.BaseInventorySerializer;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "base_inventory")
@JsonSerialize(using = BaseInventorySerializer.class)
public class BaseInventory extends Inventory {

    @DBRef
    @JsonBackReference
    private Base base;

    private long lastRefresh;

    public BaseInventory() {
        super();
        setLastRefresh(System.currentTimeMillis());
    }

    public BaseInventory(Base base){
        super();
        setBase(base);
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

    @Override
    public long getMaxVolume() {
        return (long) Math.floor(getBase().getBaseStat().getValue(BaseStat.MAX_VOLUME));
    }
}
