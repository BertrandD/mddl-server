package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.serializer.BaseInventorySerializer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = BaseInventorySerializer.class)
public class BaseInventory extends Inventory {

    @OneToOne(fetch = FetchType.LAZY)
    private Base base;

    public BaseInventory() {
        super();
    }

    public BaseInventory(Base base){
        super();
        setBase(base);
    }

    @Override
    public long getAvailableCapacity(){
        Double capaStats = getBase().getBaseStat().getStats().get(Stats.BASE_MAX_STORAGE_VOLUME);
        if (capaStats != null) {
            return capaStats.longValue();
        }
        return 0L;
    }

    @Override
    public ItemInstance getItem(String id) {
        return getItemsToMap().getOrDefault(id, null);
    }
}
