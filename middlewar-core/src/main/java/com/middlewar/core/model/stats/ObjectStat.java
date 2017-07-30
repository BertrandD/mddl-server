package com.middlewar.core.model.stats;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.serializer.ObjectStatSerializer;
import lombok.Data;
import org.apache.log4j.Logger;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
@Data
@Embeddable
@JsonSerialize(using = ObjectStatSerializer.class)
public class ObjectStat {

    @Transient
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Stats, Double> stats; // TODO : move this to List<StatHolder>

    public ObjectStat(Stats stats) {
        setStats(new HashMap<>());
        addStat(stats);
    }

    public ObjectStat() {
        setStats(new HashMap<>());
    }

    public void addStat(final Stats stat) {
        addStat(stat, 0);
    }

    public void addStat(final Stats stat, double val) {
        if (!getStats().containsKey(stat))
            getStats().put(stat, val);
        else logger.warn("Trying to replace an existing stat (" + stat.name() + "). Abort.");
    }

    public double getValue(Stats stat) {
        return getValue(stat, 0);
    }

    public double getValue(Stats stat, double defaultValue) {
        return getStats().getOrDefault(stat, defaultValue);
    }

    public void add(final Stats stat, final double val) {
        add(stat, val, StatOp.DIFF);
    }

    public void add(StatHolder statHolder) {
        if (statHolder == null) return;
        add(statHolder.getStat(), statHolder.getValue(), statHolder.getOp());
    }

    public void add(final Stats stat, final double val, final StatOp op) {

        if (!getStats().containsKey(stat) && op != StatOp.UNLOCK) {
            logger.warn("Trying an operator on an non registered stat: " + stat.name());
            return;
        }

        switch (op) {
            case DIFF:
                getStats().replace(stat, getValue(stat) + val);
                break;
            case PER:
                getStats().replace(stat, getValue(stat) * val);
                break;
            case UNLOCK:
                addStat(stat);
                add(stat, val);
                break;
            default:
                break;
        }
    }
}
