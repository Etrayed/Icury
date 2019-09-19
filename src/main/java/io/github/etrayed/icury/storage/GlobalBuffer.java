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
@SuppressWarnings("unchecked")
public class GlobalBuffer {

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

    public String getString(String key) {
        return buffer.getString(key);
    }

    public boolean getBoolean(String key) {
        return buffer.getBoolean(key);
    }

    public byte getByte(String key) {
        return buffer.getByte(key);
    }

    public short getShort(String key) {
        return buffer.getShort(key);
    }

    public int getInt(String key) {
        return buffer.getInt(key);
    }

    public long getLong(String key) {
        return buffer.getLong(key);
    }

    public double getDouble(String key) {
        return buffer.getDouble(key);
    }

    public float getFloat(String key) {
        return buffer.getFloat(key);
    }

    public Enum getEnum(String key, Class enumClass) {
        return buffer.getEnum(key, enumClass);
    }

    public void writeString(String key, String value) {
        buffer.writeString(key, value);
    }

    public void writeBoolean(String key, boolean value) {
        buffer.writeBoolean(key, value);
    }

    public void writeByte(String key, byte value) {
        buffer.writeByte(key, value);
    }

    public void writeShort(String key, short value) {
        buffer.writeShort(key, value);
    }

    public void writeInt(String key, int value) {
        buffer.writeInt(key, value);
    }

    public void writeLong(String key, long value) {
        buffer.writeLong(key, value);
    }

    public void writeDouble(String key, double value) {
        buffer.writeDouble(key, value);
    }

    public void writeFloat(String key, float value) {
        buffer.writeFloat(key, value);
    }

    public void writeEnum(String key, Enum value) {
        buffer.writeEnum(key, value);
    }

    public void remove(String key) {
        buffer.remove(key);
    }

    public Object raw() {
        return buffer.raw();
    }

    public StorageBuffer convertTo(Class bufferClass) {
        return buffer.convertTo(bufferClass);
    }
    
    public StorageBuffer getOriginal() {
        return buffer;
    }
}
