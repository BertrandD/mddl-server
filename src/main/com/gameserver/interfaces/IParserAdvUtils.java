package com.gameserver.interfaces;

import java.time.Duration;

/**
 * More advanced interface for parsers.<br>
 * Allows usage of get methods without fall back value.<br>
 * @author xban1x
 */
public interface IParserAdvUtils extends IParserUtils
{
    public boolean getBoolean(String key);

    public byte getByte(String key);

    public short getShort(String key);

    public int getInt(String key);

    public long getLong(String key);

    public float getFloat(String key);

    public double getDouble(String key);

    public String getString(String key);

    public Duration getDuration(String key);

    public <T extends Enum<T>> T getEnum(String key, Class<T> clazz);

}
