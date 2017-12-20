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
public class Engine extends SlotItem {

    private long power;

    public Engine(StatsSet set, Requirement requirement) {
        super(StructureSlotType.ENGINE, set, requirement);
        setPower(set.getLong("power", 0));
        getStats().put(Stats.POWER, new StatHolder(Stats.POWER, getPower(), StatOp.DIFF));
    }
}
