package com.middlewar.core.model.stats;

import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import lombok.Data;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
public class StatCalculator {

    private Stats stats;

    private double value;

    public StatCalculator(Stats stats) {
        setStats(stats);
    }

    public StatHolder toStatHolder() {
        return toStatHolder(StatOp.DIFF);
    }

    public StatHolder toStatHolder(StatOp op) {
        return new StatHolder(stats, value, op);
    }

    public void add(final double val) {
        add(val, StatOp.DIFF);
    }

    public void add(StatHolder statHolder) {
        if (statHolder == null) return;
        if (!statHolder.getStat().equals(stats)) return;
        add(statHolder.getValue(), statHolder.getOp());
    }

    public void add(final double val, final StatOp op) {
        switch (op) {
            case DIFF:
                setValue(getValue() + val);
                break;
            case PER:
                setValue(getValue() * val);
                break;
            case UNLOCK:
                setValue(val);
                break;
            default:
                break;
        }
    }

    public double calc(List<StatHolder> statHolders) {
        if (statHolders == null) return 0;
        for (StatHolder statHolder : statHolders) {
            this.add(statHolder);
        }
        return getValue();
    }
}
