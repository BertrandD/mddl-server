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
    public static final String GENERAL_CONFIG_FILE = "config/General.properties";

    // --------------------------------------------------
    // Variable Definitions
    // --------------------------------------------------
    public static String DATA_ROOT_DIRECTORY;
    public static long MAX_PLAYER_INVENTORY_CAPACITY;

    // --------------------------------------------------
    // Load properties files
    // --------------------------------------------------
    public static void load(String MODE)
    {
        final PropertiesParser general = new PropertiesParser(MODE+GENERAL_CONFIG_FILE);

        DATA_ROOT_DIRECTORY = general.getString("DataRootDirectory", "data/");
        MAX_PLAYER_INVENTORY_CAPACITY = general.getLong("MaxPlayerInventoryCapacity", 1000000000);
    }
}
