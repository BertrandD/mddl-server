package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.enums.BuildingType;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "base_buildings")
public class BuildingInstance {

    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonView(View.BuildingInstance_Base.class)
    private Base base;

    @JsonView(View.Standard.class)
    private String buildingId;

    @JsonView(View.Standard.class)
    private int currentHealth;

    @JsonView(View.Standard.class)
    private int currentLevel;

    @Transient
    private Building template;

    public BuildingInstance(){}

    public BuildingInstance(Base base, Building template) {
        setBase(base);
        setBuildingId(template.getId());
        setCurrentHealth(template.getMaxHp());
        setCurrentLevel(1);
        setTemplate(template);
    }

    public BuildingInstance(String id, Base base, String buildingId, int currentHealth, int currentLevel){
        setId(id);
        setBase(base);
        setBuildingId(buildingId);
        setTemplate(BuildingData.getInstance().getBuilding(buildingId));
        setCurrentHealth(currentHealth);
        setCurrentLevel(currentLevel);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Building getTemplate() {
        return template;
    }

    public void setTemplate(Building template) {
        this.template = template;
    }
}
