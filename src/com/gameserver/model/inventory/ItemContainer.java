package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.interfaces.IInventory;
import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.stats.Stats;
import com.serializer.ItemContainerSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "resource_inventory")
@JsonSerialize(using = ItemContainerSerializer.class)
public final class ItemContainer implements IInventory{

    @Id
    private String id;

    @DBRef
    @JsonBackReference
    private Base base;

    private Stats stat;

    @DBRef
    @JsonBackReference
    private ItemInstance item;

    private long lastRefresh;

    public ItemContainer(Base base, ItemInstance item) {
        setId(new ObjectId().toString());
        setBase(base);
        setStat(Stats.valueOf(item.getTemplateId().toUpperCase()));
        setItem(item);
        setLastRefresh(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Stats getStat() {
        return stat;
    }

    public void setStat(Stats stat) {
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
}
