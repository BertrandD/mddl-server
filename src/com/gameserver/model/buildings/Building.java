package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.enums.BuildingType;
import com.gameserver.model.commons.StatsSet;
import com.util.data.json.View;

/**
 * @author LEBOC Philippe
 */
public abstract class Building {

    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String name;

    @JsonView(View.Standard.class)
    private String descriptionId;

    @JsonView(View.Standard.class)
    private BuildingType type;

    @JsonView(View.Standard.class)
    private int maxLevel;

    public Building(StatsSet set){
        setId(set.getString("id"));
        setName(set.getString("name"));
        setType(set.getEnum("type", BuildingType.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));
    }

    public abstract String calcRequirement();

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

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
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
}
