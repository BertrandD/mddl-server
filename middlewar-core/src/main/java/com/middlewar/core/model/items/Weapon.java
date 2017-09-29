package com.middlewar.core.model.items;

import com.middlewar.core.enums.Slot;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.stats.Stats;

/**
 * @author LEBOC Philippe
 */
public final class Weapon extends SlotItem {

    private double damage;

    public Weapon(StatsSet set, Requirement req) {
        super(Slot.WEAPON, set, req);
        setDamage(set.getDouble("damage", 0));
        getStats().put(Stats.DAMAGE, new StatHolder(Stats.DAMAGE, getDamage(), StatOp.DIFF));
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
