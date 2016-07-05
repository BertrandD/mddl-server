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

    private HashMap<BaseStat, Double> stats;

    public ObjectStat() {
        setStats(new HashMap<>());
    }

    public void addStat(BaseStat baseStat) {
        if(!getStats().containsKey(baseStat))
            getStats().put(baseStat, baseStat.getValue());
        else logger.warn("Trying to replace an existing baseStat (" + baseStat.name() + "). Abort.");
    }

    public double getValue(BaseStat baseStat) {
        return getStats().get(baseStat);
    }

    public HashMap<BaseStat, Double> getStats() {
        return stats;
    }

    private void setStats(HashMap<BaseStat, Double> stats) {
        this.stats = stats;
    }

    public void add(final BaseStat baseStat, final double val, final StatOp op) {
        switch (op)
        {
            case DIFF: getStats().replace(baseStat, getValue(baseStat) + val); break;
            case PER: getStats().replace(baseStat, getValue(baseStat) * val); break;
            default: break;
        }
    }
}
