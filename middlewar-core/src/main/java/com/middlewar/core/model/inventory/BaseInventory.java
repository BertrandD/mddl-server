package com.middlewar.core.model.inventory;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class BaseInventory extends Inventory {

    @OneToOne(fetch = FetchType.LAZY)
    private Base base;

    public BaseInventory(Base base) {
        super();
        setBase(base);
    }

    @Override
    public long getAvailableCapacity() {
        return ((Number) getBase().getBaseStat().getValue(Stats.BASE_MAX_STORAGE_VOLUME, 10000)).longValue();
    }

    @Override
    public ItemInstance getItem(String id) {
        return getItems()
                .stream()
                .filter(i -> i.getTemplateId().equals(id))
                .findFirst().orElse(null);
    }
}
