package com.gameserver.model.buildings;

import com.gameserver.enums.BuildingType;
import com.gameserver.model.Requirement;

/**
 * @author LEBOC Philippe
 */
public class Building extends AbstractBuilding {
    public Building(String id, BuildingType type, String name, String description, int maxlevel, int maxHp, long time, Requirement requirements) {
        super(id, type, name, description, maxlevel, maxHp, time, requirements);
    }
}
