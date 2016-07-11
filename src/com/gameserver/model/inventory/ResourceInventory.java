package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.stats.BaseStat;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "resource_inventory")
public final class ResourceInventory extends AbstractInventory {

    @DBRef
    @JsonBackReference
    private Base base;

    private BaseStat stat;

    @DBRef
    @JsonBackReference
    private ItemInstance item;

    private long lastRefresh;

    public ResourceInventory(Base base, ItemInstance item) {
        super();
        setBase(base);
        setStat(BaseStat.valueOf(item.getTemplateId().toUpperCase()));
        setItem(item);
        setLastRefresh(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public BaseStat getStat() {
        return stat;
    }

    public void setStat(BaseStat stat) {
        this.stat = stat;
    }

    public ItemInstance getItem() {
        return item;
    }

    public void setItem(ItemInstance item) {
        this.item = item;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
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
        return (long)(getBase().getBaseStat().getValue(BaseStat.valueOf("MAX_" + getStat()), 0)); // TODO: check cast
    }

    @Override
    public long getVolume() {
        return (long)(getItem().getCount() * getItem().getTemplate().getVolume());
    }

    @Override
    public long getFreeVolume() {
        return Math.max(0, getMaxVolume() - getVolume());
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ResourceInventory) && (((ResourceInventory) o).getId().equals(this.getId()));
    }
}
