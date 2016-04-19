package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.enums.Lang;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.Mine;
import com.gameserver.model.buildings.Storage;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "buildings")
public class BuildingInstance
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private Base base;

    @JsonView(View.Standard.class)
    private String buildingId;

    @JsonView(View.Standard.class)
    private int currentLevel;

    @Transient
    @JsonIgnore
    private Lang lang = Lang.EN;

    public BuildingInstance(){}

    public BuildingInstance(Base base, Building template) {
        setBase(base);
        setBuildingId(template.getId());
        setCurrentLevel(1);
    }

    @JsonView(View.buildingInstance_full.class)
    public Storage getStorageBuilding(){
        if(getTemplate() instanceof Storage){
            return (Storage) getTemplate();
        }
        return null;
    }

    @JsonView(View.buildingInstance_full.class)
    public Mine getMineBuilding(){
        if(getTemplate() instanceof Mine){
            return (Mine) getTemplate();
        }
        return null;
    }

    @JsonView(View.buildingInstance_full.class)
    public Building getTemplate() {
        final Building building = BuildingData.getInstance().getBuilding(buildingId);
        building.setLang(getLang());
        return building;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    @JsonIgnore
    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }
}
