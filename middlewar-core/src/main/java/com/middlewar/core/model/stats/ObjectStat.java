package com.middlewar.core.model.stats;

import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
@Slf4j
public class ObjectStat {

    private Map<Stats, List<StatHolder>> stats;

    public ObjectStat() {
        stats = new HashMap<>();
    }

    public void unlock(final Stats stat) {
        if (!stats.containsKey(stat))
            stats.put(stat, new ArrayList<>());
        else log.warn("Trying to replace an existing stat (" + stat.name() + "). Abort.");
    }

    public void unlock(final Stats stat, double val, StatOp op) {
        unlock(stat);
        add(stat, val, op);
    }

    public double getValue(Stats stat) {
        StatCalculator statCalculator = new StatCalculator(stat);
        return statCalculator.calc(stats.get(stat));
    }

    public double getValue(Stats stat, double defaultValue) {
        if (!stats.containsKey(stat)) return defaultValue;
        return getValue(stat);
    }

    public void add(final Stats stat, final double val) {
        add(stat, val, StatOp.DIFF);
    }

    public void add(StatHolder statHolder) {
        if (statHolder == null) return;
        if (statHolder.getOp().equals(StatOp.UNLOCK))
            unlock(statHolder.getStat());
        if (stats.containsKey(statHolder.getStat()))
            stats.get(statHolder.getStat()).add(statHolder);
        else
            log.warn("Trying to add a not unlocked stat: " + statHolder.getStat().name());
    }

    public void add(final Stats stat, final double val, final StatOp op) {

        if (!stats.containsKey(stat) && op != StatOp.UNLOCK) {
            log.warn("Trying an operator on an non unlocked stat: " + stat.name());
            return;
        }

        add(new StatHolder(stat, val, op));
    }
}


