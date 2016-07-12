package com.gameserver.interfaces;

import com.gameserver.holders.StatHolder;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface IStat {
    List<StatHolder> getStats();
    void setStats(List<StatHolder> stats);
}
