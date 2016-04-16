package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public class HeadQuarter extends Building {

    public HeadQuarter(StatsSet set) {
        super(set);
    }

    @Override
    public String calcRequirement() {
        return null;
    }
}
