package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;

/**
 * @author LEBOC Philippe
 */
public class RobotFactory extends Building {

    private double[] cooldownReduction;

    public RobotFactory(StatsSet set) {
        super(set);
        initialize(set.getString("cooldown_reduction", null));
    }

    private void initialize(String function) {
        if(function == null) return;
        final double[] t = new double[getMaxLevel()];
        for(int i=1;i<=getMaxLevel();i++) {
            t[i-1] = ((Number) Evaluator.getInstance().eval(function.replace("$level", ""+i))).doubleValue();
        }
        setCooldownReduction(t);
    }

    public double getCoolDownReductionAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel()) return getCooldownReduction()[level-1];
        else return 0;
    }

    public double[] getCooldownReduction() {
        return cooldownReduction;
    }

    public void setCooldownReduction(double[] cooldownReduction) {
        this.cooldownReduction = cooldownReduction;
    }
}
