package com.gameserver.model;

import com.config.Config;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Stat;
import com.gameserver.enums.StatOp;
import com.gameserver.holders.StatHolder;
import com.gameserver.holders.StatModifierHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.commons.BaseStat;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.items.Module;
import com.serializer.BaseSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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

    @Transient
    private BaseStat baseStat;

    private HashMap<Integer, String> buildingPositions;

    @DBRef
    @JsonManagedReference
    private List<BuildingInstance> buildings;

    @DBRef
    @JsonManagedReference
    private BaseInventory baseInventory;

    public Base() {
        setBuildingPositions(new HashMap<>());
        setBuildings(new ArrayList<>());
        setBaseStat(new BaseStat());
    }

    public Base(String name, Player owner) {
        setId(new ObjectId().toString());
        setName(name);
        setOwner(owner);
        setBaseStat(new BaseStat());
        setBuildings(new ArrayList<>());
        setBuildingPositions(new HashMap<>());
    }

    /**
     * Base Stats Initializer
     * Use it when you want to serialize Base or use some stats
     *      Warning: @PostContruct does not work fine with Spring & MongoDB.
     *               That's why we need initialize stats hand made
     */
    public void initializeStats(boolean force) {
        if(!force && !getBaseStat().getStats().isEmpty()) return;

        // Intialize all existing stats
        for (Stat stat : Stat.values()) {
            getBaseStat().addStat(new StatHolder(stat));
        }

        // Buildings stats
        long energyConsumption = 0;
        for (BuildingInstance building : getBuildings()) {
            final Building template = building.getTemplate();

            for (StatModifierHolder holder : template.getStats()) {
                getBaseStat().getStat(holder.getStat()).add(template.getStatValue(holder.getStat(), building.getCurrentLevel()), holder.getOp());
            }

            energyConsumption += (template.getUseEnergyAtLevel(building.getCurrentLevel()) * Config.USE_ENERGY_MODIFIER);
        }

        // Building energy consumption
        getBaseStat().getStat(Stat.ENERGY).add(-energyConsumption, StatOp.DIFF);
    }

    public HashMap<String, Double> getProduction() {
        initializeStats(false);

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
                    production.put(entry.getKey(), entry.getValue() * getBaseStat().getStat(Stat.RESOURCE_PRODUCTION_SPEED).getValue());
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
                Double resource = production.get(module.getAffected());
                if (resource != null) {
                    resource = resource * module.getMultiplicator(); // apply module multiplicator
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
