package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.serializer.ResourceSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 *
 * This class manage only one resource item
 * A Resource object is a GameItem with an amount that can be updated during time !
 *  - When a Resource Object must be transfered to a Ship, it will be converted to a regular ItemInstance because
 *    the Resource object does not need to be updated during flying.
 */
@Data
@ToString
@Entity
@NoArgsConstructor
@JsonSerialize(using = ResourceSerializer.class)
public class Resource {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonBackReference
    private Base base;

    @OneToOne(cascade = CascadeType.ALL)
    private ItemInstance item;

    private long lastRefresh;

    @Enumerated(EnumType.STRING)
    private Stats stat;

    public Resource(Base base, ItemInstance item) {
        setBase(base);
        setItem(item);
        setLastRefresh(TimeUtil.getCurrentTime());
    }

    public long getAvailableCapacity() {
        // Add more logic here to handle building effects on capacity
        return (long)getBase().getBaseStat().getValue(stat, 0);
    }
}
