package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.SystemMessageData;
import com.gameserver.enums.BuildingType;
import com.gameserver.enums.Lang;
import com.gameserver.model.commons.StatsSet;
import com.util.data.json.View;
import org.springframework.data.annotation.Transient;

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
    private BuildingType type;

    @JsonView(View.Standard.class)
    private int maxLevel;

    @JsonView(View.Standard.class)
    private String buildTimeFunc;

    @JsonView(View.Standard.class)
    private String reqMetalFunc;

    @Transient
    @JsonIgnore
    private Lang lang = Lang.EN;

    public Building(StatsSet set){
        setId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", BuildingType.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));
        setBuildTimeFunc(set.getString("buildTimeFunc", null));
        setReqMetalFunc(set.getString("reqMetalFunc", null));
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

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
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

    public String getReqMetalFunc() {
        return reqMetalFunc;
    }

    public void setReqMetalFunc(String func) {
        this.reqMetalFunc = func;
    }

    @JsonIgnore
    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }
}
