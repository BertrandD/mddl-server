package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public class Shield extends ModulableBuilding {

    private long armor;
    private String strongAgainst;

    public Shield(StatsSet set) {
        super(set);
        setArmor(set.getLong("armor", 0L));
        setStrongAgainst(set.getString("shield_defense", null));
    }

    public long getArmor() {
        return armor;
    }

    public void setArmor(long armor) {
        this.armor = armor;
    }

    public String getStrongAgainst() {
        return strongAgainst;
    }

    public void setStrongAgainst(String strongAgainst) {
        this.strongAgainst = strongAgainst;
    }
}
