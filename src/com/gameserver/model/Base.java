package com.gameserver.model;

import com.config.Config;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.model.stats.BaseStat;
import com.gameserver.enums.StatOp;
import com.gameserver.holders.StatHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.items.Module;
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

    public Base() {
        setBuildingPositions(new HashMap<>());
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
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
    }

    /**
     * Base Stats Initializer
     * Use it when you want to serialize Base or use some stats
     *      Warning: @PostContruct does not work fine with Spring & MongoDB.
     *               That's why we need initialize stats hand made
     */
    public void initializeStats(boolean force) {
        if(!force && getBaseStat() != null) return;

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
                getBaseStat().add(holder.getBaseStat(), template.getStatValue(holder.getBaseStat(), building.getCurrentLevel()), holder.getOp());
            }

            energyConsumption += (template.getUseEnergyAtLevel(building.getCurrentLevel()) * Config.USE_ENERGY_MODIFIER);
        }

        // Building energy consumption
        getBaseStat().add(BaseStat.ENERGY, -energyConsumption, StatOp.DIFF);
    }

    public HashMap<String, Double> getProduction() {
        initializeStats(true);

        final HashMap<String, Double> production = new HashMap<>();
        final List<BuildingInstance> extractors = getBuildings().stream().filter(k ->
                k.getTemplate().getType().equals(BuildingCategory.Extractor) &&
                k.getCurrentLevel() > 0).collect(Collectors.toList());

        if(extractors.isEmpty()) return null;

        for (BuildingInstance extractor : extractors) {
            final Extractor template = ((Extractor) extractor.getTemplate());
            for (Map.Entry<String, Long> entry : template.getProductionAtLevel(extractor.getCurrentLevel()).entrySet()) {
                if (!production.containsKey(entry.getKey()))
                {
                    production.put(entry.getKey(), entry.getValue() * getBaseStat().getValue(BaseStat.RESOURCE_PRODUCTION_SPEED));
                }
                else
                {
                    double currentCnt = production.get(entry.getKey());
                    currentCnt += entry.getValue();
                    production.replace(entry.getKey(), currentCnt);
                }
            }
        }

        // Calculate resources modules bonus
        for (BuildingInstance extractor : extractors) {
            for (Module module : extractor.getModules()) {
                for (StatHolder holder : module.getStats()) {
                    getBaseStat().add(holder.getBaseStat(), holder.getValue(), holder.getOp());
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

    public void setBaseInventory(BaseInventory baseInventory) {
        this.baseInventory = baseInventory;
    }
}
