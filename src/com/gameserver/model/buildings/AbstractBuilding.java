package com.gameserver.model.buildings;

import com.gameserver.enums.BuildingType;
import com.gameserver.model.commons.Requirement;

/**
 * @author LEBOC Philippe
 */
abstract class AbstractBuilding {

    private String id;
    private String name;
    private String description;
    private BuildingType type;
    private int maxLevel;
    private int maxHp;
    private long buildTime;
    private boolean disabled;
    private Requirement requirements;

    public AbstractBuilding(String id, BuildingType type, String name, String description, int maxlevel, int maxHp, long time, Requirement requirements){
        setId(id);
        setName(name);
        setType(type);
        setDescription(description);
        setMaxLevel(maxlevel);
        setMaxHp(maxHp);
        setBuildTime(time);
        setDisabled(false);
        setRequirements(requirements);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Requirement getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirement requirements) {
        this.requirements = requirements;
    }
}
