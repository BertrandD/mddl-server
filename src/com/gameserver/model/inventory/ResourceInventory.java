package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Storage;
import com.gameserver.model.instances.BuildingInstance;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "resource_inventory")
public class ResourceInventory extends Inventory {

    private static final String STORAGE_METAL = "storage_metal";

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private Base base;

    @JsonView(View.Standard.class)
    private long lastRefresh;

    public ResourceInventory(){
        super();
    }

    public ResourceInventory(Base base){
        super();
        setBase(base);
        setLastRefresh(System.currentTimeMillis());
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    @Override
    @JsonView(View.Standard.class)
    public long getMaxVolume() {
        long volume = 0;
        final List<BuildingInstance> storages = getBase().getBuildings().stream().filter(k->k.getBuildingId().equals(STORAGE_METAL) && k.getCurrentLevel() > 0).collect(Collectors.toList());
        for (BuildingInstance storage : storages) {
            volume += ((Storage)storage.getTemplate()).getCapacityBonus(storage.getCurrentLevel());
        }
        return volume;
    }
}
