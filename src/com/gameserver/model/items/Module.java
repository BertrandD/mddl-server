package com.gameserver.model.items;

import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public final class Module extends Item {

    private String affected; // Id
    private double multiplicator;

    public Module(StatsSet set, Requirement req) {
        super(set, req);
        setAffected(set.getString("affected", null));
        setMultiplicator(set.getDouble("multiplicator", 1));
    }

    public String getAffected() {
        return affected;
    }

    public void setAffected(String affected) {
        this.affected = affected;
    }

    public double getMultiplicator() {
        return multiplicator;
    }

    public void setMultiplicator(double multiplicator) {
        this.multiplicator = multiplicator;
    }
}
