package io.github.etrayed.icury.storage;

import io.github.etrayed.icury.Icury;
import io.github.etrayed.icury.storage.json.JsonBuffer;
import io.github.etrayed.icury.storage.mongodb.MongoBuffer;
import io.github.etrayed.icury.storage.mysql.MySQLBuffer;
import io.github.etrayed.icury.storage.xml.XmlBuffer;
import io.github.etrayed.icury.storage.yaml.YamlBuffer;

/**
 * @author Etrayed
 */
public class GlobalBuffer implements StorageBuffer {

    private final StorageBuffer buffer;

    public GlobalBuffer() {
        this.buffer = chooseRightBuffer();
    }

    private StorageBuffer chooseRightBuffer() {
        switch (Icury.getConfigurationInterpreter().getStorageType()) {
            case JSON:
                return new JsonBuffer();
            case MONGODB:
                return new MongoBuffer();
            case MYSQL:
                return new MySQLBuffer();
            case XML:
                return new XmlBuffer();
            case YAML:
                return new YamlBuffer();
            default:
                throw new IllegalArgumentException("Invalid storageType: "
                        + Icury.getConfigurationInterpreter().getStorageType());
        }
    }

    @Override
    public String getString(String key) {
        return buffer.getString(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return buffer.getBoolean(key);
    }

    @Override
    public byte getByte(String key) {
        return buffer.getByte(key);
    }

    @Override
    public short getShort(String key) {
        return buffer.getShort(key);
    }

    @Override
    public int getInt(String key) {
        return buffer.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return buffer.getLong(key);
    }

    @Override
    public double getDouble(String key) {
        return buffer.getDouble(key);
    }

    @Override
    public float getFloat(String key) {
        return buffer.getFloat(key);
    }

    @Override
    public Enum getEnum(String key, Class enumClass) {
        return buffer.getEnum(key, enumClass);
    }

    @Override
    public void writeString(String key, String value) {
        buffer.writeString(key, value);
    }

    @Override
    public void writeBoolean(String key, boolean value) {
        buffer.writeBoolean(key, value);
    }

    @Override
    public void writeByte(String key, byte value) {
        buffer.writeByte(key, value);
    }

    @Override
    public void writeShort(String key, short value) {
        buffer.writeShort(key, value);
    }

    @Override
    public void writeInt(String key, int value) {
        buffer.writeInt(key, value);
    }

    @Override
    public void writeLong(String key, long value) {
        buffer.writeLong(key, value);
    }

    @Override
    public void writeDouble(String key, double value) {
        buffer.writeDouble(key, value);
    }

    @Override
    public void writeFloat(String key, float value) {
        buffer.writeFloat(key, value);
    }

    @Override
    public void writeEnum(String key, Enum value) {
        buffer.writeEnum(key, value);
    }

    @Override
    public void remove(String key) {
        buffer.remove(key);
    }

    @Override
    public Object raw() {
        return buffer.raw();
    }

    @Override
    public StorageBuffer convertTo(Class bufferClass) {
        return buffer.convertTo(bufferClass);
    }
}
