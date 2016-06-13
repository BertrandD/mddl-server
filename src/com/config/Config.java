package com.config;

import com.util.PropertiesParser;

/**
 * @author LEBOC Philippe
 */
public final class Config {

    // --------------------------------------------------
    // Initialization File Definitions
    // --------------------------------------------------
    public static final String DEV_CONFIG_DIRECTORY = "dist/";
    public static final String PROD_CONFIG_DIRECTORY = "";

    public static final String APPLICATION_CONFIG_LOCATION = "config/application.properties";
    public static final String GENERAL_CONFIG_FILE = "config/general.properties";

    // --------------------------------------------------
    // Variable Definitions
    // --------------------------------------------------
    public static String DATA_ROOT_DIRECTORY;
    public static String[] FORBIDDEN_NAMES;
    public static long MAX_PLAYER_INVENTORY_CAPACITY;
    public static String SLACK_URL;
    public static String SLACK_WARNING_CHANNEL;
    public static String SLACK_WARNING_BOT_NAME;
    public static String SLACK_WARNING_BOT_ICON;
    public static String SLACK_INFO_CHANNEL;
    public static String SLACK_INFO_BOT_NAME;
    public static String SLACK_INFO_BOT_ICON;
    public static long INFINITY;
    public static long BASE_INITIAL_MAX_SHIELD;
    public static long BASE_INITIAL_MAX_HEALTH;

    // --------------------------------------------------
    // Load properties files
    // --------------------------------------------------
    public static void load(String MODE)
    {
        final PropertiesParser general = new PropertiesParser(MODE+GENERAL_CONFIG_FILE);

        DATA_ROOT_DIRECTORY = general.getString("DataRootDirectory", "data/");
        FORBIDDEN_NAMES = general.getString("ForbiddenNames", "").split(",");
        MAX_PLAYER_INVENTORY_CAPACITY = general.getLong("MaxPlayerInventoryCapacity", 1000000000);
        INFINITY = general.getLong("InfinityValue", -1);
        BASE_INITIAL_MAX_SHIELD = general.getLong("InitialMaxShield", 0);
        BASE_INITIAL_MAX_HEALTH = general.getLong("InitialMaxHealth", 0);

        // Slack
        SLACK_URL = general.getString("SlackURL", "");
        SLACK_WARNING_CHANNEL = general.getString("SlackWarningChannel", "");
        SLACK_WARNING_BOT_NAME = general.getString("SlackWarningBotName", "");
        SLACK_WARNING_BOT_ICON = general.getString("SlackWarningBotIcon", "");
        SLACK_INFO_CHANNEL = general.getString("SlackInfoChannel", "");
        SLACK_INFO_BOT_NAME = general.getString("SlackInfoBotName", "");
        SLACK_INFO_BOT_ICON = general.getString("SlackInfoBotIcon", "");
    }
}
