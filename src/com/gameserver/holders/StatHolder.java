package com.gameserver.holders;

import com.gameserver.model.stats.BaseStat;
import com.gameserver.enums.StatOp;

/**
 * @author LEBOC Philippe
 */
public class StatHolder {

    private BaseStat stat;
    private StatOp op;
    private double value; // direct baseStat value
    private double[] values; // buildings: values indexed by levels

    public StatHolder(BaseStat baseStat, StatOp op) {
        setStat(baseStat);
        setOp(op);
    }

    public BaseStat getStat() {
        return stat;
    }

    private void setStat(BaseStat stat) {
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
        if(level < 1 || level > values.length)
            if(op.equals(StatOp.DIFF)) return 0; else return 1;
        return values[level-1];
    }
}
