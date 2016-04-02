package com.gameserver.holders;

import com.gameserver.enums.BuildingType;

/**
 * @author LEBOC Philippe
 */
public class BuildingHolder {

    private BuildingType type;
    private int level;

    public BuildingHolder(BuildingType type, int level){
        setType(type);
        setLevel(level);
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
