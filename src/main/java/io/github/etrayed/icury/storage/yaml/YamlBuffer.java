package io.github.etrayed.icury.storage.yaml;

import io.github.etrayed.icury.storage.StorageBuffer;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Etrayed
 */
public class YamlBuffer implements StorageBuffer<YamlConfiguration> {

    private final YamlConfiguration instance;

    public YamlBuffer(YamlConfiguration instance) {
        this.instance = instance;
    }

    public YamlBuffer() {
        this(new YamlConfiguration());
    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public boolean getBoolean(String key) {
        return false;
    }

    @Override
    public byte getByte(String key) {
        return 0;
    }

    @Override
    public short getShort(String key) {
        return 0;
    }

    @Override
    public int getInt(String key) {
        return 0;
    }

    @Override
    public long getLong(String key) {
        return 0;
    }

    @Override
    public double getDouble(String key) {
        return 0;
    }

    @Override
    public float getFloat(String key) {
        return 0;
    }

    @Override
    public <T extends Enum<T>> Enum<T> getEnum(String key, Class<T> enumClass) {
        return null;
    }

    @Override
    public void writeString(String key, String value) {

    }

    @Override
    public void writeBoolean(String key, boolean value) {

    }

    @Override
    public void writeByte(String key, byte value) {

    }

    @Override
    public void writeShort(String key, short value) {

    }

    @Override
    public void writeInt(String key, int value) {

    }

    @Override
    public void writeLong(String key, long value) {

    }

    @Override
    public void writeDouble(String key, double value) {

    }

    @Override
    public void writeFloat(String key, float value) {

    }

    @Override
    public <T extends Enum<T>> void writeEnum(String key, Enum<T> value) {

    }

    @Override
    public YamlConfiguration raw() {
        return instance;
    }

    @Override
    public <T extends StorageBuffer> T convertTo(Class<T> bufferClass) {
        return null;
    }
}
