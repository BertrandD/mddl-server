package com.gameserver.model.items;

import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.stats.BaseStat;

/**
 * @author LEBOC Philippe
 */
public final class Module extends Item {

    private BaseStat unlockStat;

    public Module(StatsSet set, Requirement req) {
        super(set, req);
        setUnlockStat(set.getEnum("unlock_stat", BaseStat.class, BaseStat.NONE));
    }

    public BaseStat getUnlockStat() {
        return unlockStat;
    }

    public void setUnlockStat(BaseStat unlockStat) {
        this.unlockStat = unlockStat;
    }
}
