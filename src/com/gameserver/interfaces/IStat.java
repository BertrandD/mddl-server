package com.gameserver.interfaces;

import com.gameserver.enums.Stat;
import com.gameserver.holders.StatModifierHolder;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface IStat {
    void setStats(List<StatModifierHolder> stats);
    List<StatModifierHolder> getStats();
    double getStatValue(Stat stat, int level);
}
