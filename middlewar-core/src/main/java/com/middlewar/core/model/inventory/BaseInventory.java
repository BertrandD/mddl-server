package com.middlewar.core.model.inventory;

import com.middlewar.core.config.Config;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
public class BaseInventory extends Inventory {

    @OneToOne(fetch = FetchType.LAZY)
    private Base base;

    public BaseInventory() {
        super();
    }

    public BaseInventory(Base base) {
        super();
        setBase(base);
    }

    @Override
    public long getAvailableCapacity() {
        return ((Number) getBase().getBaseStat().getValue(Stats.BASE_MAX_STORAGE_VOLUME, Config.BASE_INITIAL_MAX_RESOURCE_STORAGE)).longValue();
    }

    @Override
    public ItemInstance getItem(String id) {
        return getItemsToMap().getOrDefault(id, null);
    }
}
