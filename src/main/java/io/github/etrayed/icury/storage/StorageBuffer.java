package io.github.etrayed.icury.storage;

/**
 * @author Etrayed
 */
public interface StorageBuffer<R> {

    String getString(String key);

    boolean getBoolean(String key);

    byte getByte(String key);

    short getShort(String key);

    int getInt(String key);

    long getLong(String key);

    double getDouble(String key);

    float getFloat(String key);

    <T extends Enum<T>> Enum<T> getEnum(String key, Class<T> enumClass);

    void writeString(String key, String value);

    void writeBoolean(String key, boolean value);

    void writeByte(String key, byte value);

    void writeShort(String key, short value);

    void writeInt(String key, int value);

    void writeLong(String key, long value);

    void writeDouble(String key, double value);

    void writeFloat(String key, float value);

    <T extends Enum<T>> void writeEnum(String key, Enum<T> value);

    R raw();

    <T extends StorageBuffer> T convertTo(Class<T> bufferClass);
}
