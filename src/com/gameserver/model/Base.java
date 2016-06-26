package com.gameserver.model;

import com.config.Config;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.buildings.PowerFactory;
import com.gameserver.model.commons.BaseStat;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.items.Module;
import com.serializer.BaseSerializer;
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
@JsonSerialize(using = BaseSerializer.class)
@Document(collection = "bases")
public final class Base
{
    @Id
    private String id;
    private String name;

    @DBRef
    @JsonManagedReference
    private Player owner;
    private BaseStat baseStat;
    private HashMap<Integer, String> buildingPositions;

    @DBRef
    @JsonManagedReference
    private List<BuildingInstance> buildings;

    @DBRef
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

    public HashMap<String, Long> getProduction() {
        final HashMap<String, Long> production = new HashMap<>();
        final List<BuildingInstance> extractors = getBuildings().stream().filter(k ->
                k.getTemplate().getType().equals(BuildingCategory.Extractor) &&
                k.getCurrentLevel() > 0).collect(Collectors.toList());

        if(extractors.isEmpty()) return null;

        for (BuildingInstance extractor : extractors) {
            final Extractor template = ((Extractor) extractor.getTemplate());
            for (Map.Entry entry : template.getProductionAtLevel(extractor.getCurrentLevel()).entrySet()) {
                if (!production.containsKey(entry.getKey().toString())) {
                    production.put(entry.getKey().toString(), (Long) entry.getValue());
                } else {
                    long currentCnt = production.get(entry.getKey().toString());
                    currentCnt += (Long) entry.getValue();
                    production.replace(entry.getKey().toString(), currentCnt);
                }
            }
        }

        // Calculate resources modules bonus
        for (BuildingInstance extractor : extractors) {
            for (Module module : extractor.getModules()) {
                Long resource = production.get(module.getAffected());
                if (resource != null) {
                    resource = (long) (resource * module.getMultiplicator()); // apply module multiplicator
                    production.replace(module.getAffected(), resource);
                }
            }
        }

        return production;
    }

    public long getEnergy() {
        final List<BuildingInstance> powerFactories = getBuildings().stream().filter(k ->
                k.getTemplate().getType().equals(BuildingCategory.PowerFactory) &&
                k.getCurrentLevel() > 0).collect(Collectors.toList());

        if(powerFactories.isEmpty()) return 0;

        long totalGeneratedPower = 0;

        // How many power generated from all PowerFactories
        for (BuildingInstance powerFactory : powerFactories) {
            final PowerFactory factory = (PowerFactory) powerFactory.getTemplate();
            totalGeneratedPower += factory.getPowerAtLevel(powerFactory.getCurrentLevel());
        }

        // How many is consumed from all building
        for (BuildingInstance buildingInstance : getBuildings()) {
            final Building building = buildingInstance.getTemplate();
            totalGeneratedPower -= building.getUseEnergyAtLevel(buildingInstance.getCurrentLevel());
        }

        return totalGeneratedPower;
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

    public BaseInventory getBaseInventory() {
        return baseInventory;
    }

    public void setBaseInventory(BaseInventory baseInventory) {
        this.baseInventory = baseInventory;
    }
}
