package com.gameserver.model.buildings;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.serializer.BuildingSerializer;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
@JsonSerialize(using = BuildingSerializer.class)
public abstract class Building {

    private String id;
    private String nameId;
    private String descriptionId;
    private BuildingCategory type;
    private int maxLevel;
    private long[] buildTimes;
    private long[] useEnergy;
    private HashMap<Integer, Requirement> requirements;
    private Lang lang = Lang.EN;

    public Building(StatsSet set) {
        setId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", BuildingCategory.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));

        setRequirements(new HashMap<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getName(){
        return SystemMessageData.getInstance().getMessage(getLang(), getNameId());
    }

    public BuildingCategory getType() {
        return type;
    }

    public void setType(BuildingCategory type) {
        this.type = type;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getDescription(){
        return SystemMessageData.getInstance().getMessage(getLang(), getDescriptionId());
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxlevel) {
        this.maxLevel = maxlevel;
    }

    public long[] getUseEnergy() {
        return useEnergy;
    }

    public void setUseEnergy(long[] useEnergy) {
        this.useEnergy = useEnergy;
    }

    public long getUseEnergyAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel())
            return getUseEnergy()[level-1];
        else return 0;
    }

    public long[] getBuildTimes() {
        return buildTimes;
    }

    public void setBuildTimes(long[] buildTimes) {
        this.buildTimes = buildTimes;
    }

    public void setRequirements(HashMap<Integer, Requirement> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Integer, Requirement> getRequirements() {
        return requirements;
    }

    public long getBuildTimeAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel()) return getBuildTimes()[level-1];
        else return 0;
    }

    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Building){
            final Building building = (Building) o;
            if(building.getId().equalsIgnoreCase(this.getId())) return true;
        }
        return false;
    }
}
