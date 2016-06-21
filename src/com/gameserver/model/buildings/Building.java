package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;
import com.util.data.json.View;

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
    private HashMap<Integer, Long> requiredEnergy;

    @JsonIgnore
    private String buildTimeFunc;

    @JsonIgnore
    private HashMap<Integer, Requirement> requirements;

    @JsonIgnore
    private Lang lang = Lang.EN;

    public Building(StatsSet set){
        setId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", BuildingCategory.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));
        setBuildTimeFunc(set.getString("buildTimeFunc", null));
        setRequiredEnergy(new HashMap<>());
        setAllRequirements(new HashMap<>());

        initialize(set);
    }

    private void initialize(StatsSet set) {
        final String useEnergyFunction = set.getString("useEnergy", null);

        for(int i=1;i<=getMaxLevel();i++)
        {
            if(useEnergyFunction != null) {
                final long prod = ((Number) Evaluator.getInstance().eval(useEnergyFunction.replace("$level", ""+i))).longValue();
                getRequiredEnergy().put(i, prod);
            } else getRequiredEnergy().put(i, 0L);
        }
    }

    @JsonView(View.Standard.class)
    @SuppressWarnings("unused")
    public long[] getBuildTimeByLevel() {
        long[] times = new long[getMaxLevel()];
        for(int i=0;i<getMaxLevel();i++){
            final String strFunc = getBuildTimeFunc().replace("$level", ""+i);
            if(strFunc != null){
                final long prod = ((Number) Evaluator.getInstance().eval(getBuildTimeFunc().replace("$level", ""+i))).longValue();
                times[i] = prod * 1000;
            }
        }
        return times;
    }

    @JsonView(View.Standard.class)
    @SuppressWarnings("unused")
    public Requirement[] getRequirements() {
        final Requirement[] req = new Requirement[getAllRequirements().size()];
        return getAllRequirements().values().toArray(req);
    }

    @JsonView(View.Standard.class)
    @SuppressWarnings("unused")
    public long[] getReqEnergy() {
        final long[] req = new long[getRequiredEnergy().size()];
        for(int i=0;i<getRequiredEnergy().size();i++){
            req[i] = getRequiredEnergyAtLevel(i+1);
        }
        return req;
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

    public HashMap<Integer, Long> getRequiredEnergy() {
        return requiredEnergy;
    }

    public void setRequiredEnergy(HashMap<Integer, Long> requiredEnergy) {
        this.requiredEnergy = requiredEnergy;
    }

    public long getRequiredEnergyAtLevel(int level) {
        return getRequiredEnergy().get(level);
    }

    public String getBuildTimeFunc() {
        return buildTimeFunc;
    }

    public void setBuildTimeFunc(String buildTimeFunc) {
        this.buildTimeFunc = buildTimeFunc;
    }

    public HashMap<Integer, Requirement> getAllRequirements() {
        return requirements;
    }

    public void setAllRequirements(HashMap<Integer, Requirement> requirements) {
        this.requirements = requirements;
    }

    @JsonIgnore
    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }
}
