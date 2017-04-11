package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.serializer.ResourceSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "resources")
@JsonSerialize(using = ResourceSerializer.class)
public final class Resource {

    @Id
    private String id;

    @DBRef
    @JsonBackReference
    private Base base;

    @DBRef
    private ItemInstance item;

    private long lastRefresh;

    private Stats stat;

    public Resource(Base base, ItemInstance item) {
        setId(new ObjectId().toString());
        setBase(base);
        setItem(item);
        setLastRefresh(TimeUtil.getCurrentTime());
    }

    public long getAvailableCapacity() {
        // Add more logic here to handle building effects on capacity
        return (long)getBase().getBaseStat().getValue(stat, 0);
    }
}
