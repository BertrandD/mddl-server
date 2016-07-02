package com.gameserver.enums;

import com.config.Config;

/**
 * @author LEBOC Philippe
 */
public enum Stat {
    ENERGY(0),
    MAX_HEALTH(Config.BASE_INITIAL_MAX_HEALTH),
    MAX_SHIELD(Config.BASE_INITIAL_MAX_SHIELD),
    MAX_VOLUME(1000),
    BUILD_COOLDOWN_REDUCTION(Config.BUILDTIME_MODIFIER),
    RESOURCE_PRODUCTION_SPEED(Config.RESOURCE_PRODUCTION_MODIFIER),
    SHIP_SPEED(1.0);

    private double baseValue;

    Stat(double baseValue) {
        setBaseValue(baseValue);
    }

    public double getBaseValue() {
        return baseValue;
    }

    private void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }
}
