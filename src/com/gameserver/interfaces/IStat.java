package com.gameserver.interfaces;

import com.gameserver.model.stats.BaseStat;
import com.gameserver.holders.StatHolder;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface IStat {

    List<StatHolder> getStats();

    void setStats(List<StatHolder> stats);

    StatHolder getStat(BaseStat baseStat);

    default double getStatValue(BaseStat baseStat, int level) {
        return getStat(baseStat).getValue(level);
    }
}
