package com.gameserver.model.stats;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.enums.Stat;
import com.gameserver.holders.StatHolder;
import com.serializer.BaseStatSerializer;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
@JsonSerialize(using = BaseStatSerializer.class)
public class BaseStat {

    private HashMap<Stat, StatHolder> stats;

    public BaseStat() {
        setStats(new HashMap<>());
    }

    public void addStat(StatHolder holder) {
        if(!getStats().containsKey(holder.getStat())) getStats().put(holder.getStat(), holder);
    }

    public StatHolder getStat(Stat stat) {
        return getStats().get(stat);
    }

    public HashMap<Stat, StatHolder> getStats() {
        return stats;
    }

    private void setStats(HashMap<Stat, StatHolder> stat) {
        this.stats = stat;
    }
}
