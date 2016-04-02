package com.gameserver.interfaces;

import java.time.Duration;

/**
 * Simple interface for parser, enforces of a fall back value.<br>
 * More suitable for developers not sure about their data.<br>
 * @author xban1x
 */
public interface IParserUtils
{
    public boolean getBoolean(String key, boolean defaultValue);

    public byte getByte(String key, byte defaultValue);

    public short getShort(String key, short defaultValue);

    public int getInt(String key, int defaultValue);

    public long getLong(String key, long defaultValue);

    public float getFloat(String key, float defaultValue);

    public double getDouble(String key, double defaultValue);

    public String getString(String key, String defaultValue);

    public Duration getDuration(String key, Duration defaultValue);

    public <T extends Enum<T>> T getEnum(String key, Class<T> clazz, T defaultValue);
}
