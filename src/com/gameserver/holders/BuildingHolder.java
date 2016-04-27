package com.gameserver.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.buildings.Building;
import com.util.data.json.View;

/**
 * @author LEBOC Philippe
 */
public class BuildingHolder {

    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
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
