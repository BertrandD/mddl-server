package com.gameserver.model.stats;

import com.config.Config;

/**
 * @author LEBOC Philippe
 */
public enum BaseStat {
    NONE(0),
    ENERGY(0),

    // Production
    RESOURCE_FEO(0),
    RESOURCE_C(0),
    RESOURCE_CH4(0),
    RESOURCE_H2O(0),
    RESOURCE_ATO3(0),

    // Resource storage
    MAX_RESOURCE_FEO(10000),
    MAX_RESOURCE_C(10000),
    MAX_RESOURCE_CH4(1000),
    MAX_RESOURCE_H2O(1000),
    MAX_RESOURCE_ATO3(10000),

    // Base general items storage
    MAX_VOLUME(1000),
    MAX_WIEGHT(0),

    // Base
    HEALTH(0),
    SHIELD(0),
    MAX_HEALTH(Config.BASE_INITIAL_MAX_HEALTH),
    MAX_SHIELD(Config.BASE_INITIAL_MAX_SHIELD),

    // Univers
    BUILD_COOLDOWN_REDUCTION(Config.BUILDTIME_MODIFIER),
    RESOURCE_PRODUCTION_SPEED(Config.RESOURCE_PRODUCTION_MODIFIER), // TODO: remove ?
    SHIP_SPEED(1.0);

    private double value;

    BaseStat(double value)
    {
        setValue(value);
    }

    public double getValue() {
        return value;
    }

    private void setValue(double value) {
        this.value = value;
    }
}
