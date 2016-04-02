package com.gameserver.model.items;

import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public final class Weapon extends Item {

    private int damage;

    public Weapon(StatsSet set, Requirement req)
    {
        super(set, req);
        setDamage(set.getInt("damage", 0));
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
