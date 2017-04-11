package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.serializer.BaseInventorySerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Data
@Document(collection = "base_inventory")
@JsonSerialize(using = BaseInventorySerializer.class)
public final class BaseInventory extends Inventory {

    @DBRef(lazy = true)
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

    @Override
    public long getAvailableCapacity(){
        return getBase().getBaseStat().getStats().get(Stats.BASE_MAX_STORAGE_VOLUME).longValue();
    }

    @Override
    public ItemInstance getItem(String id) {
        return getItemsToMap().getOrDefault(id, null);
    }
}
