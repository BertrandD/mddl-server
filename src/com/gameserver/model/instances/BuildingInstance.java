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
    private int currentHealth;

    @JsonView(View.Standard.class)
    private int currentLevel;

    @Transient
    private Building template;

    @JsonView(View.Standard.class)
    private BuildingType type;

    public BuildingInstance(){}

    public BuildingInstance(Base base, Building template) {
        setId(null);
        setBase(base);
        setCurrentHealth(template.getMaxHp());
        setCurrentLevel(1);
        setTemplate(template);
        setType(template.getType());
    }

    public BuildingInstance(String id, Base base, BuildingType type, int currentHealth, int currentLevel){
        setId(id);
        setBase(base);
        setTemplate(BuildingData.getInstance().getBuilding(type));
        setCurrentHealth(currentHealth);
        setCurrentLevel(currentLevel);
        setType(type);
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

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }
}
