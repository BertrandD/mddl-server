package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.Storage;
import com.gameserver.model.inventory.StorageBuildingInventory;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
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
    @JsonView(View.BuildingInstance_Base.class)
    private Base base;

    @JsonView(View.Standard.class)
    private String buildingId;

    @JsonView(View.Standard.class)
    private int currentHealth;

    @JsonView(View.Standard.class)
    private int currentLevel;

    @DBRef
    @JsonBackReference
    @JsonView(View.BuildingInstance_Inventory.class)
    private StorageBuildingInventory inventory;

    public BuildingInstance(){}

    public BuildingInstance(Base base, Building template) {
        setBase(base);
        setBuildingId(template.getId());
        setCurrentHealth(template.getMaxHp());
        setCurrentLevel(1);
    }

    @JsonIgnore
    public Storage getStorageBuilding(){
        if(getTemplate() instanceof Storage){
            return (Storage) getTemplate();
        }
        return null;
    }

    @JsonIgnore
    public Building getTemplate() {
        return BuildingData.getInstance().getBuilding(buildingId);
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

    public StorageBuildingInventory getInventory() {
        return inventory;
    }

    public void setInventory(StorageBuildingInventory inventory) {
        this.inventory = inventory;
    }
}
