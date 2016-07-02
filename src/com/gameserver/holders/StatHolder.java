package com.gameserver.holders;

import com.gameserver.enums.Stat;
import com.gameserver.enums.StatOp;

/**
 * @author LEBOC Philippe
 */
public class StatHolder {

    private Stat stat;
    private double value;

    public StatHolder(Stat stat) {
        setStat(stat);
        setValue(stat.getBaseValue());
    }

    public Stat getStat() {
        return stat;
    }

    private void setStat(Stat stat) {
        this.stat = stat;
    }

    public double getValue() {
        return value;
    }

    private void setValue(double value) {
        this.value = value;
    }

    public void add(final double val, final StatOp op) {
        switch (op)
        {
            case DIFF:
                setValue(getValue() + val);
                break;
            case PER:
                setValue(getValue() * val);
                break;
            default:
                break;
        }
    }
}
