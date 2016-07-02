package com.gameserver.holders;

import com.gameserver.enums.Stat;
import com.gameserver.enums.StatOp;

/**
 * @author LEBOC Philippe
 */
public class StatModifierHolder<T> {

    private Stat stat;
    private StatOp op;

    public StatModifierHolder(Stat stat, StatOp op) {
        setStat(stat);
        setOp(op);
    }

    public Stat getStat() {
        return stat;
    }

    private void setStat(Stat stat) {
        this.stat = stat;
    }

    public StatOp getOp() {
        return op;
    }

    private void setOp(StatOp op) {
        this.op = op;
    }
}
