package com.middlewar.core.model.inventory;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.utils.TimeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 * <p>
 * This class manage only one resource item
 * A Resource object is a GameItem with an amount that can be updated during time !
 * - When a Resource Object must be transfered to a Ship, it will be converted to a regular ItemInstance because
 * the Resource object does not need to be updated during flying.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Resource {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Base base;

    @OneToOne(cascade = CascadeType.ALL)
    private ItemInstance item;

    private long lastRefresh;

    //@Enumerated(EnumType.STRING)
    //private Stats stat;

    public Resource(Base base, ItemInstance item) {
        setBase(base);
        setItem(item);
        setLastRefresh(TimeUtil.getCurrentTime());
    }

    public double getCount() {
        return getItem().getCount();
    }

    public Stats getStat() {
        return Stats.valueOf(item.getTemplateId().toUpperCase());
    }

    public Stats getStatMax() {
        return Stats.valueOf("MAX_" + item.getTemplateId().toUpperCase());
    }

    public long calcAvailableCapacity() {
        return base.calcResourceStorageAvailableCapacity(this);
    }

    public double calcProdPerHour() {
        return base.calcResourceProduction(this);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Resource && ((Resource) o).getId() == getId();
    }
}
