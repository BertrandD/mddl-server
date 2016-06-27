package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;

/**
 * @author LEBOC Philippe
 */
public class Storage extends Building {

    private long[] capacity;

    public Storage(StatsSet set){
        super(set);
        initialize(set.getString("capacity"));
    }

    private void initialize(String function){
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
}
