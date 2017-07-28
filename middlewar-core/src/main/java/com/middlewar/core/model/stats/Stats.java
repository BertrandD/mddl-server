package com.middlewar.core.model.stats;

import com.middlewar.core.config.Config;

/**
 * @author LEBOC Philippe
 */
public enum Stats {

    // Others
    NONE,
    ENERGY,

    // Current amount of every Resource
    RESOURCE_1,
    RESOURCE_2,
    RESOURCE_4,
    RESOURCE_5,
    RESOURCE_3,

    // Max Resource storage affected by buildings
    MAX_RESOURCE_1,
    MAX_RESOURCE_2,
    MAX_RESOURCE_4,
    MAX_RESOURCE_5,
    MAX_RESOURCE_3,

    // Base
    BASE_HEALTH,
    BASE_SHIELD,
    BASE_MAX_HEALTH,
    BASE_MAX_SHIELD,

    // GameItem storage (!= Resource)
    BASE_MAX_STORAGE_VOLUME,

    // Fleet
    FLEET_HEALTH,
    FLEET_MAX_HEALTH,
    FLEET_SHIELD,
    FLEET_MAX_SHIELD,
    FLEET_DAMAGE,
    FLEET_CARGO,

    // Univers
    BUILD_COOLDOWN_REDUCTION
}
