package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.enums.BuildingCategory;
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
@Document(collection = "base_inventory")
public class BaseInventory extends Inventory {

    private static final String STORAGE = "storage";

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private Base base;

    public BaseInventory(){
        super();
    }

    public BaseInventory(Base base){
        super();
        setBase(base);
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    @Override
    @JsonView(View.Standard.class)
    public long getMaxVolume() {
        long volume = 0;
        final List<BuildingInstance> storages = getBase().getBuildings().stream().filter(k->k.getBuildingId().equals(STORAGE) &&
                k.getTemplate().getType().equals(BuildingCategory.Storage) &&
                k.getCurrentLevel() > 0).collect(Collectors.toList());
        for (BuildingInstance storage : storages) {
            volume += ((Storage)storage.getTemplate()).getCapacityBonus(storage.getCurrentLevel());
        }
        return volume;
    }
}
