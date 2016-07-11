package com.gameserver.model.buildings;

import com.gameserver.enums.StatOp;
import com.gameserver.holders.StatHolder;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.stats.BaseStat;

import java.util.Collections;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Silo extends ModulableBuilding {

    public Silo(StatsSet set) {
        super(set);
    }

    @Override
    public List<StatHolder> getStats() {
        if(getModules().size() != 1) return Collections.singletonList(new StatHolder(BaseStat.NONE, StatOp.NONE));
        return Collections.singletonList(super.getStat(getModules().get(0).getUnlockStat()));
    }
}
