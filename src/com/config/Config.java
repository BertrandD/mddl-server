package com.config;

import com.util.PropertiesParser;

/**
 * @author LEBOC Philippe
 */
public final class Config {

    // --------------------------------------------------
    // Initialization File Definitions
    // --------------------------------------------------
    public static final String GENERAL_CONFIG_FILE = "../../dist/Config/General.properties";
    //public static final String RATES_CONFIG_FILE = "./config/Rates.properties";


    // --------------------------------------------------
    // Variable Definitions
    // --------------------------------------------------
    public static String DATA_ROOT_DIRECTORY;



    // --------------------------------------------------
    // Load properties files
    // --------------------------------------------------
    public static void load()
    {
        final PropertiesParser general = new PropertiesParser(GENERAL_CONFIG_FILE);

        DATA_ROOT_DIRECTORY = general.getString("DataRootDirectory", "../../dist/data");
    }
}
