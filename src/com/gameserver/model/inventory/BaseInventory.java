package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.model.Base;
import com.gameserver.model.stats.BaseStat;
import com.serializer.BaseInventorySerializer;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "base_inventory")
@JsonSerialize(using = BaseInventorySerializer.class)
public final class BaseInventory extends AbstractMultiStorageInventory {

    @DBRef
    @JsonBackReference
    private Base base;

    public BaseInventory() {
        super();
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

    @Override
    public long getMaxWeight() {
        return 0;
    }

    @Override
    public long getWeight() {
        return 0;
    }

    @Override
    public long getFreeWeight() {
        return 0;
    }

    @Override
    public long getMaxVolume() {
        return (long) Math.floor(getBase().getBaseStat().getValue(BaseStat.MAX_VOLUME));
    }

    @Override
    public long getVolume() {
        return 0;
    }

    @Override
    public long getFreeVolume() {
        return getMaxVolume() - getVolume();
    }
}
