package com.gameserver.model.buildings;

import com.gameserver.enums.BuildingType;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public abstract class Building {

    private String id;
    private String name;
    private String descriptionId;
    private BuildingType type;
    private int maxLevel;
    private int maxHp;
    private long buildTime;
    private boolean disabled;
    private HashMap<Integer, Requirement> requirements;

    public Building(StatsSet set){
        setId(set.getString("id"));
        setName(set.getString("name"));
        setType(set.getEnum("type", BuildingType.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));
        setMaxHp(set.getInt("maxHealth"));
        setBuildTime(set.getLong("buildTime"));
        setDisabled(set.getBoolean("disabled", false));
        setRequirements(new HashMap<>());
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

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxlevel) {
        this.maxLevel = maxlevel;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public HashMap<Integer, Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<Integer, Requirement> requirements) {
        this.requirements = requirements;
    }
}
