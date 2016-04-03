package com.gameserver.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.buildings.Building;

/**
 * @author LEBOC Philippe
 */
public class BuildingHolder {

    private String id;
    private int level;

    public BuildingHolder(String id, int level){
        setId(id);
        setLevel(level);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JsonIgnore
    public Building getTemplate(){
        return BuildingData.getInstance().getBuilding(id);
    }
}
