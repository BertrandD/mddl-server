package com.gameserver.model.stats;

import com.config.Config;

/**
 * @author LEBOC Philippe
 */
public enum Stats {

    // Others
    NONE(0),
    ENERGY(0),

    // Production
    RESOURCE_FEO(0),
    RESOURCE_C(0),
    RESOURCE_CH4(0),
    RESOURCE_H2O(0),
    RESOURCE_ATO3(0),

    // Resource storage
    MAX_RESOURCE_FEO(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_C(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_CH4(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_H2O(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_ATO3(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),

    // Base
    BASE_HEALTH(0),
    BASE_SHIELD(0),
    BASE_MAX_HEALTH(Config.BASE_INITIAL_MAX_HEALTH),
    BASE_MAX_SHIELD(Config.BASE_INITIAL_MAX_SHIELD),
    BASE_MAX_STORAGE_VOLUME(1000),

    // Fleet
    FLEET_HEALTH(0),
    FLEET_MAX_HEALTH(0),
    FLEET_SHIELD(0),
    FLEET_MAX_SHIELD(0),
    FLEET_DAMAGE(0),
    FLEET_CARGO(0),

    // Univers
    BUILD_COOLDOWN_REDUCTION(Config.BUILDTIME_MODIFIER);

    private double value;

    Stats(double value)
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
