package com.gameserver.model.items;

import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public class Engine extends Item {

    private long power;

    public Engine(StatsSet set, Requirement requirement) {
        super(set, requirement);
        setPower(set.getLong("power", 0));
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }
}
