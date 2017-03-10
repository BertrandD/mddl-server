package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.serializer.ResourceSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 *
 * This class manage only one resource item
 */
@Data
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
        setLastRefresh(System.currentTimeMillis());
    }

    public long getAvailableCapacity() {
        return (long)getBase().getBaseStat().getValue(stat, 0);
    }
}
