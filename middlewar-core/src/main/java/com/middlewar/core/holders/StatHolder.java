package com.middlewar.core.holders;

import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.dto.holder.StatHolderDTO;
import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
public class StatHolder {

    private Stats stat;
    private StatOp op;
    private double value; // direct baseStat value

    public StatHolder(Stats stats, double value, StatOp op) {
        setStat(stats);
        setOp(op);
        setValue(value);
    }

}
