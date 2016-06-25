package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.util.data.json.View;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public abstract class Building {

    @JsonView(View.Standard.class)
    private String id;

    @JsonIgnore
    private String nameId;

    @JsonIgnore
    private String descriptionId;

    @JsonView(View.Standard.class)
    private BuildingCategory type;

    @JsonView(View.Standard.class)
    private int maxLevel;

    @JsonIgnore
    private HashMap<Integer, Long> buildTimes;

    @JsonView(View.Standard.class)
    private long[] useEnergy;

    @JsonIgnore
    private HashMap<Integer, Requirement> requirements;

    @JsonIgnore
    private Lang lang = Lang.EN;

    public Building(StatsSet set) {
        setId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", BuildingCategory.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));

        setBuildTimes(new HashMap<>());
        setAllRequirements(new HashMap<>());
    }

    @JsonView(View.Standard.class)
    @SuppressWarnings("unused")
    public long[] getBuildTimeByLevel() {
        final Collection<Long> values = getBuildTimes().values();
        final long[] result = new long[values.size()];
        for(int i=0;i<values.size();i++){
            result[i] = getBuildTimeAtLevel(i + 1);
        }
        return result;
    }

    @JsonView(View.Standard.class)
    @SuppressWarnings("unused")
    public Requirement[] getRequirements() {
        final Requirement[] req = new Requirement[getAllRequirements().size()];
        return getAllRequirements().values().toArray(req);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    private String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    @JsonView(View.Standard.class)
    public String getName(){
        return SystemMessageData.getInstance().getMessage(getLang(), getNameId());
    }

    public BuildingCategory getType() {
        return type;
    }

    public void setType(BuildingCategory type) {
        this.type = type;
    }

    @JsonIgnore
    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    @JsonView(View.Standard.class)
    @SuppressWarnings("unused")
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

    @JsonIgnore
    public HashMap<Integer, Requirement> getAllRequirements() {
        return requirements;
    }

    public void setAllRequirements(HashMap<Integer, Requirement> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Integer, Long> getBuildTimes() {
        return buildTimes;
    }

    public void setBuildTimes(HashMap<Integer, Long> buildTimes) {
        this.buildTimes = buildTimes;
    }

    public long getBuildTimeAtLevel(int level) {
        return getBuildTimes().get(level);
    }

    @JsonIgnore
    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }
}
