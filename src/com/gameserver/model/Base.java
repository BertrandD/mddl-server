package com.gameserver.model;

import com.config.Config;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.enums.StatOp;
import com.gameserver.holders.StatHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.model.items.Module;
import com.gameserver.model.stats.BaseStat;
import com.gameserver.model.stats.ObjectStat;
import com.serializer.BaseSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private long currentHealth;
    private long currentShield;
    private double healthRegenRate;
    private double shieldRegenRate;
    private HashMap<Integer, String> buildingPositions;

    @DBRef
    @Indexed
    @JsonManagedReference
    private Player owner;

    @Transient
    private ObjectStat baseStat;

    @DBRef
    @JsonManagedReference
    private List<BuildingInstance> buildings;

    @DBRef
    @JsonManagedReference
    private BaseInventory baseInventory;

    @DBRef
    @JsonManagedReference
    private List<ResourceInventory> resources;

    public Base() {
        setBuildingPositions(new HashMap<>());
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
        setResources(new ArrayList<>());
    }

    public Base(String name, Player owner) {
        setId(new ObjectId().toString());
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setBuildingPositions(new HashMap<>());
        setCurrentHealth(Config.BASE_INITIAL_MAX_HEALTH);
        setCurrentShield(Config.BASE_INITIAL_MAX_SHIELD);
        setHealthRegenRate(100.0);
        setShieldRegenRate(100.0);
        setBaseStat(new ObjectStat());
        setResources(new ArrayList<>());
    }

    /**
     * Base Stats Initializer
     * Use it when you want to serialize Base or use some stats
     *      Warning: @PostContruct does not work fine with Spring & MongoDB.
     *               That's why we need initialize stats hand made
     */
    public void initializeStats() {
        // Intialize all existing stats
        for (BaseStat baseStat : BaseStat.values()) {
            getBaseStat().addStat(baseStat);
        }

        // Base stats
        getBaseStat().add(BaseStat.HEALTH, getCurrentHealth(), StatOp.DIFF);
        getBaseStat().add(BaseStat.SHIELD, getCurrentShield(), StatOp.DIFF);

        // Buildings stats
        long energyConsumption = 0;
        for (BuildingInstance building : getBuildings()) {
            final Building template = building.getTemplate();

            for (StatHolder holder : template.getStats()) {
                getBaseStat().add(holder.getStat(), template.getStatValue(holder.getStat(), building.getCurrentLevel()), holder.getOp());
            }

            energyConsumption += (template.getUseEnergyAtLevel(building.getCurrentLevel()) * Config.USE_ENERGY_MODIFIER);
        }

        // Building energy consumption
        getBaseStat().add(BaseStat.ENERGY, -energyConsumption, StatOp.DIFF);

        // Module
        for (BuildingInstance inst : getBuildings()) {
            for (Module module : inst.getModules()) {
                for (StatHolder holder : module.getStats()) {
                    getBaseStat().add(holder.getStat(), holder.getValue(), holder.getOp());
                }
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

    public long getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(long currentHealth) {
        this.currentHealth = currentHealth;
    }

    public long getCurrentShield() {
        return currentShield;
    }

    public void setCurrentShield(long currentShield) {
        this.currentShield = currentShield;
    }

    /**
     * @return the amount of SHIELD regeneration per hour
     */
    public double getHealthRegenRate() {
        return healthRegenRate;
    }

    public void setHealthRegenRate(double healthRegenRate) {
        this.healthRegenRate = healthRegenRate;
    }

    /**
     * @return the amount of HP regeneration per hour
     */
    public double getShieldRegenRate() {
        return shieldRegenRate;
    }

    public void setShieldRegenRate(double shieldRegenRate) {
        this.shieldRegenRate = shieldRegenRate;
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

    public List<ResourceInventory> getResources() {
        return resources;
    }

    public void setResources(final List<ResourceInventory> resources) {
        this.resources = resources;
    }

    public void addResourceInventory(final ResourceInventory inventory) {
        this.resources.add(inventory);
    }
}
