package com.gameserver.model;

import com.config.Config;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.ItemType;
import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.commons.BaseStat;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.model.items.CommonItem;
import com.gameserver.model.items.Module;
import com.util.data.json.View;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "bases")
public final class Base
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String name;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private Player owner;

    @JsonView(View.Standard.class)
    private BaseStat baseStat;

    @JsonView(View.Standard.class)
    private HashMap<Integer, String> buildingPositions;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private List<BuildingInstance> buildings;

    @DBRef
    @JsonIgnore
    @JsonManagedReference
    private ResourceInventory resourcesInventory;

    @DBRef
    @JsonIgnore
    @JsonManagedReference
    private BaseInventory baseInventory;

    public Base() {
        setBuildings(new ArrayList<>());
        setBuildingPositions(new HashMap<>());
    }

    public Base(String name, Player owner) {
        setId(new ObjectId().toString());
        setName(name);
        setOwner(owner);
        setBaseStat(new BaseStat(Config.BASE_INITIAL_MAX_SHIELD, Config.BASE_INITIAL_MAX_HEALTH));
        setBuildings(new ArrayList<>());
        setBuildingPositions(new HashMap<>());
    }

    @SuppressWarnings("unused")
    @JsonView(View.Standard.class)
    public HashMap<String, Long> getMaxVolumes() {
        final HashMap<String, Long> inventoriesVolumes = new HashMap<>();
        inventoriesVolumes.put("max_volume_resources", getResourcesInventory().getMaxVolume());
        inventoriesVolumes.put("max_volume_items", getBaseInventory().getMaxVolume());
        return inventoriesVolumes;
    }

    @JsonView(View.Standard.class)
    public HashMap<String, List<ItemInstance>> getInventory() {
        final HashMap<String, List<ItemInstance>> inventory = new HashMap<>();
        inventory.put(ItemType.RESOURCE.toString(), getResourcesInventory().getItems());
        inventory.put(ItemType.CARGO.toString(), getBaseInventory().getItems().stream().filter(ItemInstance::isCargo).collect(Collectors.toList()));
        inventory.put(ItemType.ENGINE.toString(), getBaseInventory().getItems().stream().filter(ItemInstance::isEngine).collect(Collectors.toList()));
        inventory.put(ItemType.MODULE.toString(), getBaseInventory().getItems().stream().filter(ItemInstance::isModule).collect(Collectors.toList()));
        inventory.put(ItemType.STRUCTURE.toString(), getBaseInventory().getItems().stream().filter(ItemInstance::isStructure).collect(Collectors.toList()));
        inventory.put(ItemType.WEAPON.toString(), getBaseInventory().getItems().stream().filter(ItemInstance::isWeapon).collect(Collectors.toList()));
        return inventory;
    }

    @SuppressWarnings("unused")
    @JsonView(View.Standard.class)
    public HashMap<String, Long> getProduction() {
        final HashMap<String, Long> production = new HashMap<>();
        final List<BuildingInstance> extractors = getBuildings().stream().filter(k ->
                k.getTemplate().getType().equals(BuildingCategory.Extractor) &&
                k.getCurrentLevel() > 0).collect(Collectors.toList());

        // If extractor count < 1 return 0 for all productions.
        if(extractors.isEmpty()) {
            for(CommonItem item : ItemData.getInstance().getResources()) {
                production.put(item.getItemId(), 0L);
            }
            return production;
        }

        for (BuildingInstance extractor : extractors)
        {
            final Extractor template = ((Extractor) extractor.getTemplate());
            for(Map.Entry entry : template.getProductionAtLevel(extractor.getCurrentLevel()).entrySet())
            {
                if(!production.containsKey(entry.getKey().toString())) {
                    production.put(entry.getKey().toString(), (Long) entry.getValue());
                } else {
                    long currentCnt = production.get(entry.getKey().toString());
                    currentCnt += (Long) entry.getValue();
                    production.replace(entry.getKey().toString(), currentCnt);
                }
            }
        }

        // Calculate resources modules bonus
        for (BuildingInstance extractor : extractors){
            for(Module module : extractor.getModules()){
                Long resource = production.get(module.getAffected());
                if(resource != null) {
                    resource = (long)(resource * module.getMultiplicator()); // apply module multiplicator
                    production.replace(module.getAffected(), resource);
                }
            }
        }

        return production;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public BaseStat getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(BaseStat baseStat) {
        this.baseStat = baseStat;
    }

    public HashMap<Integer, String> getBuildingPositions() {
        return buildingPositions;
    }

    public void setBuildingPositions(HashMap<Integer, String> buildingPositions) {
        this.buildingPositions = buildingPositions;
    }

    @JsonView(View.Standard.class)
    public List<BuildingInstance> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingInstance> buildings) {
        this.buildings = buildings;
    }

    public void addBuilding(BuildingInstance building, int position) {
        this.buildings.add(building);
        this.buildingPositions.put(position, building.getId());
    }

    @JsonIgnore
    public ResourceInventory getResourcesInventory() {
        return resourcesInventory;
    }

    public void setResourcesInventory(ResourceInventory resources) {
        this.resourcesInventory = resources;
    }

    @JsonIgnore
    public BaseInventory getBaseInventory() {
        return baseInventory;
    }

    public void setBaseInventory(BaseInventory baseInventory) {
        this.baseInventory = baseInventory;
    }
}
