package com.gameserver.model.buildings;

import com.gameserver.holders.FuncHolder;
import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class PowerFactory extends Building {

    private FuncHolder powerFunction;

    private HashMap<Integer, Long> power;

    public PowerFactory(StatsSet set) {
        super(set);
        setPowerFunction(new FuncHolder(null, set.getString("production")));
        init();
    }

    private void init() {
        final HashMap<Integer, Long> tpower = new HashMap<>();
        for(int i = 1; i <= getMaxLevel(); i++)
        {
            final long prod = ((Number) Evaluator.getInstance().eval(powerFunction.getFunction().replace("$level", ""+i))).longValue();
            tpower.put(i, prod);
        }
        setPower(tpower);
    }

    public FuncHolder getPowerFunction() {
        return powerFunction;
    }

    public void setPowerFunction(FuncHolder powerFunction) {
        this.powerFunction = powerFunction;
    }

    public long getPowerAtLevel(int level) {
        return getPower().get(level);
    }

    public HashMap<Integer, Long> getPower() {
        return power;
    }

    public void setPower(HashMap<Integer, Long> power) {
        this.power = power;
    }
}
