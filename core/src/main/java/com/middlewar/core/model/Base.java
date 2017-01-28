package com.middlewar.core.model;

import com.middlewar.core.config.Config;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.ItemContainer;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.vehicles.Fleet;
import com.middlewar.core.model.vehicles.Ship;
import com.middlewar.core.serializer.BaseSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private HashMap<Integer, String> buildingPositions;

    @DBRef
    @Indexed
    @JsonManagedReference
    private Player owner;

    @Transient
    private ObjectStat baseStat;

    @DBRef
    @JsonManagedReference
    private List<Ship> ships;

    @DBRef
    private List<Fleet> fleets;

    @DBRef
    @JsonManagedReference
    private List<BuildingInstance> buildings;

    @DBRef
    @JsonManagedReference
    private BaseInventory baseInventory;

    @DBRef
    @JsonManagedReference
    private List<ItemContainer> resources;

    public Base() {
        setBuildingPositions(new HashMap<>());
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
        setResources(new ArrayList<>());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
    }

    public Base(String name, Player owner) {
        setId(new ObjectId().toString());
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setBuildingPositions(new HashMap<>());
        setBaseStat(new ObjectStat());
        setResources(new ArrayList<>());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
    }

    /**
     * Base Stats Initializer
     * Use it when you want to serialize Base or use some stats
     *      Warning: @PostContruct does not work fine with Spring & MongoDB.
     *               That's why we need initialize stats hand made
     */
    public void initializeStats() {
        final ObjectStat stats = getBaseStat();

        // Register base stats
        stats.addStat(Stats.BASE_HEALTH);
        stats.addStat(Stats.BASE_MAX_HEALTH);
        stats.addStat(Stats.BASE_SHIELD);
        stats.addStat(Stats.BASE_MAX_SHIELD);

        stats.addStat(Stats.ENERGY);
        stats.addStat(Stats.BASE_MAX_STORAGE_VOLUME);

        stats.addStat(Stats.RESOURCE_FEO);
        stats.addStat(Stats.RESOURCE_C);
        stats.addStat(Stats.RESOURCE_ATO3);
        stats.addStat(Stats.RESOURCE_CH4);
        stats.addStat(Stats.RESOURCE_H2O);

        // Applying module that's unlock stats.
        final List<BuildingInstance> silos = getBuildings().stream().filter(r -> r.getBuildingId().equalsIgnoreCase("silo")).collect(Collectors.toList());
        if(silos != null && !silos.isEmpty()) {
            silos.forEach(silo -> silo.getModules().forEach(module -> module.handleEffect(stats))); // Warning: CAN'T WORK IF WE ADD OTHERS STATS THAN MAX_RESOURCE_XX UNLOCK
        }

        // Applying buildings stats
        long energyConsumption = 0;
        for (BuildingInstance building : getBuildings()) {
            final Building template = building.getTemplate();
            template.handleEffect(stats, building.getCurrentLevel());
            energyConsumption += (template.getUseEnergyAtLevel(building.getCurrentLevel()) * Config.USE_ENERGY_MODIFIER);
        }

        // Building energy consumption
        stats.add(Stats.ENERGY, -energyConsumption, StatOp.DIFF);

        // Applying Module stats
        for (BuildingInstance inst : getBuildings()) {
            for (Module module : inst.getModules()) {
                module.handleEffect(stats);
            }
        }
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

    public ObjectStat getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(ObjectStat baseStat) {
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

    public void setBaseInventory(final BaseInventory baseInventory) {
        this.baseInventory = baseInventory;
    }

    public List<ItemContainer> getResources() {
        return resources;
    }

    public void setResources(final List<ItemContainer> resources) {
        this.resources = resources;
    }

    public void addResourceInventory(final ItemContainer inventory) {
        this.resources.add(inventory);
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public List<Fleet> getFleets() {
        return fleets;
    }

    public void setFleets(List<Fleet> fleets) {
        this.fleets = fleets;
    }
}
