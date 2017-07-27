package com.middlewar.core.holders;

import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.stats.Stats;

/**
 * @author LEBOC Philippe
 * TODO: REFACTOR !
 */
public class StatHolder {

    private Stats stat;
    private StatOp op;
    private double value; // direct baseStat value
    private double[] values; // buildings: values indexed by levels

    public StatHolder(Stats stats, StatOp op) {
        setStat(stats);
        setOp(op);
    }

    public Stats getStat() {
        return stat;
    }

    private void setStat(Stats stat) {
        this.stat = stat;
    }

    public StatOp getOp() {
        return op;
    }

    private void setOp(StatOp op) {
        this.op = op;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public double getValue(int level) {
        if (level < 1 || level > values.length)
            if (op.equals(StatOp.DIFF)) return 0;
            else return 1;
        return values[level - 1];
    }
}
