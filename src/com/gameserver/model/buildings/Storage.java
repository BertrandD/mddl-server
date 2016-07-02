package com.gameserver.model.buildings;

import com.gameserver.enums.Stat;
import com.gameserver.holders.StatModifierHolder;
import com.gameserver.interfaces.IStat;
import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Storage extends Building implements IStat {

    private List<StatModifierHolder> stats;
    private long[] capacity;

    public Storage(StatsSet set){
        super(set);
        initialize(set.getString("capacity", null));
    }

    private void initialize(String function){
        if(function == null) return;
        final long[] ms = new long[getMaxLevel()];
        for(int i = 1; i <= getMaxLevel(); i++)
            ms[i-1] = ((Number) Evaluator.getInstance().eval(function.replace("$level", ""+i))).longValue();
        setCapacity(ms);
    }

    public long getCapacityAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel())
            return getCapacity()[level-1];
        else return 0;
    }

    public long[] getCapacity() {
        return capacity;
    }

    public void setCapacity(long[] capacity) {
        this.capacity = capacity;
    }

    @Override
    public void setStats(List<StatModifierHolder> stats) {
        this.stats = stats;
    }

    @Override
    public List<StatModifierHolder> getStats() {
        return stats;
    }

    @Override
    public double getStatValue(Stat stat, int level) {
        final double val;
        switch(stat) {
            case MAX_VOLUME: val = getCapacityAtLevel(level); break;
            default: val = 0;
        }
        return val;
    }
}
