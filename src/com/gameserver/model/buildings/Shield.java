package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;

/**
 * @author LEBOC Philippe
 */
public class Shield extends ModulableBuilding {

    private long[] armorBonus;

    public Shield(StatsSet set) {
        super(set);
        initialize(set.getString("armor", null));
    }

    private void initialize(String function) {
        if(function == null) return;
        final long[] t = new long[getMaxLevel()];
        for(int i=1;i<=getMaxLevel();i++) {
            t[i-1] = ((Number) Evaluator.getInstance().eval(function.replace("$level", ""+i))).longValue();
        }
        setArmorBonus(t);
    }

    public long getArmorBonusAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel())
            return getArmorBonus()[level-1];
        return 0;
    }

    public long[] getArmorBonus() {
        return armorBonus;
    }

    public void setArmorBonus(long[] armorBonus) {
        this.armorBonus = armorBonus;
    }
}
