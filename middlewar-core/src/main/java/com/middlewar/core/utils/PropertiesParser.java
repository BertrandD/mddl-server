package com.middlewar.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Properties;

/**
 * Simplifies loading of property files and adds logging if a non existing property is requested.
 *
 * @author NosBit
 */
@Slf4j
public final class PropertiesParser {

    private final Properties _properties = new Properties();
    private final File _file;

    public PropertiesParser(String name) {
        this(new File(name));
    }

    public PropertiesParser(File file) {
        _file = file;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.defaultCharset())) {
                _properties.load(inputStreamReader);
            }
        } catch (Exception e) {
            log.warn("[" + _file.getName() + "] There was an error loading config reason: " + e.getMessage());
        }
    }

    public boolean containskey(String key) {
        return _properties.containsKey(key);
    }

    private String getValue(String key) {
        final String value = _properties.getProperty(key);
        return value != null ? value.trim() : null;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"boolean\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public byte getByte(String key, byte defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"byte\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public short getShort(String key, short defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"short\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"int\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"long\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public float getFloat(String key, float defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"float\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public double getDouble(String key, double defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be \"double\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    public String getString(String key, String defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }
        return value;
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> clazz, T defaultValue) {
        final String value = getValue(key);
        if (value == null) {
            log.warn("[" + _file.getName() + "] missing property for key: " + key + " using default value: " + defaultValue);
            return defaultValue;
        }

        try {
            return Enum.valueOf(clazz, value);
        } catch (IllegalArgumentException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + key + " specified value: " + value + " should be enum value of \"" + clazz.getSimpleName() + "\" using default value: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * @param durationPattern
     * @param defaultValue
     * @return {@link Duration} object by the durationPattern specified, {@code null} in case of malformed pattern.
     */
    public Duration getDuration(String durationPattern, String defaultValue) {
        return getDuration(durationPattern, defaultValue, null);
    }

    /**
     * @param durationPattern
     * @param defaultValue
     * @param defaultDuration
     * @return {@link Duration} object by the durationPattern specified, the defaultDuration in case of malformed pattern.
     */
    public Duration getDuration(String durationPattern, String defaultValue, Duration defaultDuration) {
        final String value = getString(durationPattern, defaultValue);
        try {
            return TimeUtil.parseDuration(value);
        } catch (IllegalStateException e) {
            log.warn("[" + _file.getName() + "] Invalid value specified for key: " + durationPattern + " specified value: " + value + " should be time patttern using default value: " + defaultValue);
        }
        return defaultDuration;
    }
}
