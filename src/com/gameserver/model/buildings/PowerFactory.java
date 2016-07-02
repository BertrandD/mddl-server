package com.gameserver.model.buildings;

import com.gameserver.enums.Stat;
import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;

/**
 * @author LEBOC Philippe
 */
public class PowerFactory extends Building {

    private long[] power;

    public PowerFactory(StatsSet set) {
        super(set);
        initialize(set.getString("production", null));
    }

    @Override
    public double getStatValue(Stat stat, int level) {
        final double val;
        switch(stat) {
            case ENERGY: val = getPowerAtLevel(level); break;
            default: val = 0;
        }
        return val;
    }

    private void initialize(String function) {
        if(function == null) return;
        final long[] p = new long[getMaxLevel()];
        for(int i = 1; i <= getMaxLevel(); i++)
            p[i-1] = ((Number) Evaluator.getInstance().eval(function.replace("$level", ""+i))).longValue();
        setPower(p);
    }

    public long getPowerAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel())
            return getPower()[level-1];
        else return 0;
    }

    public long[] getPower() {
        return power;
    }

    public void setPower(long[] power) {
        this.power = power;
    }
}
