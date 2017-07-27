package com.middlewar.core.model.stats;

import com.middlewar.core.config.Config;

/**
 * @author LEBOC Philippe
 */
public enum Stats {

    // Others
    NONE(0),
    ENERGY(0),

    // Current amount of every Resource
    RESOURCE_1(0),
    RESOURCE_2(0),
    RESOURCE_4(0),
    RESOURCE_5(0),
    RESOURCE_3(0),

    // Max Resource storage affected by buildings
    MAX_RESOURCE_1(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_2(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_4(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_5(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),
    MAX_RESOURCE_3(Config.BASE_INITIAL_MAX_RESOURCE_STORAGE),

    // Base
    BASE_HEALTH(0),
    BASE_SHIELD(0),
    BASE_MAX_HEALTH(Config.BASE_INITIAL_MAX_HEALTH),
    BASE_MAX_SHIELD(Config.BASE_INITIAL_MAX_SHIELD),

    // GameItem storage (!= Resource)
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

    Stats(double value) {
        setValue(value);
    }

    public double getValue() {
        return value;
    }

    private void setValue(double value) {
        this.value = value;
    }
}
