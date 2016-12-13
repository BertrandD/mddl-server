package com.middlewar.core.interfaces;

import com.middlewar.core.holders.StatHolder;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface IStat {
    List<StatHolder> getStats();
    void setStats(List<StatHolder> stats);
}
