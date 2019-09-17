package io.github.etrayed.icury.storage.mysql;

import io.github.etrayed.icury.storage.StorageBuffer;
import io.github.etrayed.icury.storage.json.JsonBuffer;
import io.github.etrayed.icury.storage.mongodb.MongoBuffer;
import io.github.etrayed.icury.storage.xml.XmlBuffer;
import io.github.etrayed.icury.storage.yaml.YamlBuffer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Etrayed
 */
public class MySQLBuffer implements StorageBuffer<Map<String, Object>> {

    private final Map<String, Object> instance;

    public MySQLBuffer(Map<String, Object> instance) {
        this.instance = instance;
    }

    public MySQLBuffer() {
        this(new HashMap<>());
    }

    @Override
    public String getString(String key) {
        return (String) instance.get(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) instance.get(key);
    }

    @Override
    public byte getByte(String key) {
        return ((Number) instance.get(key)).byteValue();
    }

    @Override
    public short getShort(String key) {
        return ((Number) instance.get(key)).shortValue();
    }

    @Override
    public int getInt(String key) {
        return ((Number) instance.get(key)).intValue();
    }

    @Override
    public long getLong(String key) {
        return ((Number) instance.get(key)).longValue();
    }

    @Override
    public double getDouble(String key) {
        return ((Number) instance.get(key)).doubleValue();
    }

    @Override
    public float getFloat(String key) {
        return ((Number) instance.get(key)).floatValue();
    }

    @Override
    public <T extends Enum<T>> Enum<T> getEnum(String key, Class<T> enumClass) {
        return Enum.valueOf(enumClass, (String) instance.get(key));
    }

    @Override
    public void writeString(String key, String value) {
        instance.put(key, value);
    }

    @Override
    public void writeBoolean(String key, boolean value) {
        instance.put(key, value);
    }

    @Override
    public void writeByte(String key, byte value) {
        instance.put(key, value);
    }

    @Override
    public void writeShort(String key, short value) {
        instance.put(key, value);
    }

    @Override
    public void writeInt(String key, int value) {
        instance.put(key, value);
    }

    @Override
    public void writeLong(String key, long value) {
        instance.put(key, value);
    }

    @Override
    public void writeDouble(String key, double value) {
        instance.put(key, value);
    }

    @Override
    public void writeFloat(String key, float value) {
        instance.put(key, value);
    }

    @Override
    public <T extends Enum<T>> void writeEnum(String key, Enum<T> value) {
        instance.put(key, value.name());
    }

    @Override
    public void remove(String key) {
        instance.remove(key);
    }

    @Override
    public Map<String, Object> raw() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends StorageBuffer> T convertTo(Class<T> bufferClass) {
        StorageBuffer buffer;

        if(JsonBuffer.class.equals(bufferClass)) {
            buffer = new JsonBuffer();
        } else if(MongoBuffer.class.equals(bufferClass)) {
            buffer = new MongoBuffer();
        } else if(MySQLBuffer.class.equals(bufferClass)) {
            return (T) new MySQLBuffer(instance);
        } else if(XmlBuffer.class.equals(bufferClass)) {
            return (T) new XmlBuffer(instance);
        } else if(YamlBuffer.class.equals(bufferClass)) {
            return (T) new YamlBuffer(instance);
        } else {
            throw new InternalError("Unconvertable BufferClass: " + (bufferClass == null ? "null"
                    : bufferClass.getCanonicalName()));
        }

        writeToBuffer(buffer);

        return (T) buffer;
    }

    private void writeToBuffer(StorageBuffer buffer) {
        for (Map.Entry<String, Object> keyEntry : instance.entrySet()) {
            if(keyEntry.getValue() instanceof String) {
                buffer.writeString(keyEntry.getKey(), (String) keyEntry.getValue());
            } else if(keyEntry.getValue() instanceof Boolean) {
                buffer.writeBoolean(keyEntry.getKey(), (Boolean) keyEntry.getValue());
            } else if(keyEntry.getValue() instanceof Number) {
                buffer.writeDouble(keyEntry.getKey(), ((Number) keyEntry.getValue()).doubleValue());
            } else {
                throw new InternalError(); // WTF
            }
        }
    }
}
