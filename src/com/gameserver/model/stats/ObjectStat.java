package com.gameserver.model.stats;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.enums.StatOp;
import com.serializer.ObjectStatSerializer;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
@JsonSerialize(using = ObjectStatSerializer.class)
public class ObjectStat {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private HashMap<Stats, Double> stats;

    public ObjectStat() {
        setStats(new HashMap<>());
    }

    public void addStat(final Stats stat) {
        if(!getStats().containsKey(stat))
            getStats().put(stat, stat.getValue());
        else logger.warn("Trying to replace an existing stat (" + stat.name() + "). Abort.");
    }

    public double getValue(Stats stat) {
        return getValue(stat, 0);
    }

    public double getValue(Stats stat, double defaultValue) {
        return getStats().get(stat) == null ? defaultValue : getStats().get(stat);
    }

    public HashMap<Stats, Double> getStats() {
        return stats;
    }

    private void setStats(HashMap<Stats, Double> stats) {
        this.stats = stats;
    }

    public void add(final Stats stat, final double val, final StatOp op) {

        if(!getStats().containsKey(stat) && op != StatOp.UNLOCK) {
            logger.warn("Trying an operator on an non registered stat: "+stat.name());
            return;
        }

        switch (op) {
            case DIFF: getStats().replace(stat, getValue(stat) + val); break;
            case PER: getStats().replace(stat, getValue(stat) * val); break;
            case UNLOCK: addStat(stat); break;
            default: break;
        }
    }
}
