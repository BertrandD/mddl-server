package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.util.data.json.View;
import org.springframework.data.annotation.Transient;

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

    @JsonView(View.Standard.class)
    private String buildTimeFunc;

    @JsonView(View.Standard.class)
    private HashMap<Integer, Requirement> requirements;

    @Transient
    @JsonIgnore
    private Lang lang = Lang.EN;

    public Building(StatsSet set){
        setId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", BuildingCategory.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));
        setBuildTimeFunc(set.getString("buildTimeFunc", null));
        setRequirements(new HashMap<>());
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
    public String getDescription(){
        return SystemMessageData.getInstance().getMessage(getLang(), getDescriptionId());
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxlevel) {
        this.maxLevel = maxlevel;
    }

    public String getBuildTimeFunc() {
        return buildTimeFunc;
    }

    public void setBuildTimeFunc(String buildTimeFunc) {
        this.buildTimeFunc = buildTimeFunc;
    }

    public HashMap<Integer, Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<Integer, Requirement> requirements) {
        this.requirements = requirements;
    }

    public void addRequirements(int level, Requirement req){
        requirements.put(level, req);
    }

    public Requirement getRequirements(int level){
        return getRequirements().get(level);
    }

    @JsonIgnore
    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }
}
