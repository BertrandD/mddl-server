package com.middlewar.core.config;

import com.middlewar.core.utils.PropertiesParser;

/**
 * @author LEBOC Philippe
 */
public final class Config {

    // --------------------------------------------------
    // Initialization File Definitions
    // --------------------------------------------------
    public static final String APPLICATION_CONFIG_LOCATION = "resources/config/";
    public static final String GENERAL_CONFIG_FILE = "resources/general.properties";
    public static final String UNIVERS_CONFIG_FILE = "resources/univers.properties";

    // --------------------------------------------------
    // Variable Definitions
    // --------------------------------------------------
    // General
    public static String DATA_ROOT_DIRECTORY;
    public static String[] FORBIDDEN_NAMES;
    public static long MAX_PLAYER_INVENTORY_CAPACITY;
    public static int MAX_PLAYER_IN_ACCOUNT;
    public static long BASE_INITIAL_MAX_SHIELD;
    public static long BASE_INITIAL_MAX_HEALTH;
    public static long BASE_INITIAL_MAX_RESOURCE_STORAGE;

    // Slack
    public static String SLACK_URL;
    public static String SLACK_WARNING_CHANNEL;
    public static String SLACK_WARNING_BOT_NAME;
    public static String SLACK_WARNING_BOT_ICON;
    public static String SLACK_INFO_CHANNEL;
    public static String SLACK_INFO_BOT_NAME;
    public static String SLACK_INFO_BOT_ICON;

    // Univers
    public static double BUILDTIME_MODIFIER;
    public static double RESOURCE_PRODUCTION_MODIFIER;
    public static double SHIP_SPEED_MODIFIER;
    public static double USE_ENERGY_MODIFIER;
    public static double PRODUCE_ENERGY_MODIFIER;

    // --------------------------------------------------
    // Load properties files
    // --------------------------------------------------
    public static void load() {
        final PropertiesParser general = new PropertiesParser(GENERAL_CONFIG_FILE);
        final PropertiesParser univers = new PropertiesParser(UNIVERS_CONFIG_FILE);

        DATA_ROOT_DIRECTORY = general.getString("DataRootDirectory", "resources/data/");
        FORBIDDEN_NAMES = general.getString("ForbiddenNames", "").split(",");
        MAX_PLAYER_INVENTORY_CAPACITY = general.getLong("MaxPlayerInventoryCapacity", 9999999);
        MAX_PLAYER_IN_ACCOUNT = general.getInt("MaxPlayerInAccount", 1);
        BASE_INITIAL_MAX_SHIELD = general.getLong("InitialMaxShield", 0);
        BASE_INITIAL_MAX_HEALTH = general.getLong("InitialMaxHealth", 0);
        BASE_INITIAL_MAX_RESOURCE_STORAGE = general.getLong("InitialMaxResourceStorage", 500);

        // Slack
        SLACK_URL = general.getString("SlackURL", "https://hooks.slack.com/services/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");
        SLACK_WARNING_CHANNEL = general.getString("SlackWarningChannel", "#warnings");
        SLACK_WARNING_BOT_NAME = general.getString("SlackWarningBotName", "Game API");
        SLACK_WARNING_BOT_ICON = general.getString("SlackWarningBotIcon", ":warning:");
        SLACK_INFO_CHANNEL = general.getString("SlackInfoChannel", "#info");
        SLACK_INFO_BOT_NAME = general.getString("SlackInfoBotName", "Game API");
        SLACK_INFO_BOT_ICON = general.getString("SlackInfoBotIcon", ":information_source:");

        // Univers
        BUILDTIME_MODIFIER = univers.getDouble("BuildTimeModifier", 1.0);
        RESOURCE_PRODUCTION_MODIFIER = univers.getDouble("ResourceProductionModifier", 1.0);
        SHIP_SPEED_MODIFIER = univers.getDouble("ShipSpeedModifier", 1.0);
        USE_ENERGY_MODIFIER = univers.getDouble("UseEnergyModifier", 1.0);
        PRODUCE_ENERGY_MODIFIER = univers.getDouble("ProduceEnergyModifier", 1.0);
    }
}
