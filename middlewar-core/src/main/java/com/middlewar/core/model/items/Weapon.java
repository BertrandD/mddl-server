package com.middlewar.core.model.items;

import com.middlewar.core.enums.StructureSlotType;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.stats.Stats;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public final class Weapon extends SlotItem {

    private double damage;

    public Weapon(StatsSet set, Requirement req) {
        super(StructureSlotType.WEAPON, set, req);
        setDamage(set.getDouble("damage", 0));
        getStats().put(Stats.DAMAGE, new StatHolder(Stats.DAMAGE, getDamage(), StatOp.DIFF));
    }
}
