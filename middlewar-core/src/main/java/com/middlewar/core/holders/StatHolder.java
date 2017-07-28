package com.middlewar.core.holders;

import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.stats.Stats;
import lombok.Data;

/**
 * @author LEBOC Philippe
 * TODO: REFACTOR !
 */
@Data
public class StatHolder {

    private Stats stat;
    private StatOp op;
    private double value; // direct baseStat value
    private double[] values; // buildings: values indexed by levels

    public StatHolder(Stats stats, StatOp op, double value) {
        setStat(stats);
        setOp(op);
        setValue(value);
    }

//    public StatHolder(Stats stats, StatOp op) {
//        setStat(stats);
//        setOp(op);
//    }

    public double getValue(int level) {
        if (level < 1 || level > values.length)
            if (op.equals(StatOp.DIFF)) return 0;
            else return 1;
        return values[level - 1];
    }
}
