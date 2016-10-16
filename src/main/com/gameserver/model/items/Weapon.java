package com.gameserver.model.items;

import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public final class Weapon extends Item {

    private double damage;

    public Weapon(StatsSet set, Requirement req)
    {
        super(set, req);
        setDamage(set.getDouble("damage", 0));
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
