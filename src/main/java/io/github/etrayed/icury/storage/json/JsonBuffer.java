package io.github.etrayed.icury.storage.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.google.gson.JsonPrimitive;
import io.github.etrayed.icury.storage.StorageBuffer;
import io.github.etrayed.icury.storage.mongodb.MongoBuffer;
import io.github.etrayed.icury.storage.mysql.MySQLBuffer;
import io.github.etrayed.icury.storage.xml.XmlBuffer;
import io.github.etrayed.icury.storage.yaml.YamlBuffer;

import java.util.Map;

/**
 * @author Etrayed
 */
public class JsonBuffer implements StorageBuffer<JsonObject> {

    private final JsonObject instance;

    public JsonBuffer(JsonObject instance) {
        this.instance = instance;
    }

    public JsonBuffer() {
        this(new JsonObject());
    }

    @Override
    public String getString(String key) {
        return instance.get(key).getAsString();
    }

    @Override
    public boolean getBoolean(String key) {
        return instance.get(key).getAsBoolean();
    }

    @Override
    public byte getByte(String key) {
        return instance.get(key).getAsByte();
    }

    @Override
    public short getShort(String key) {
        return instance.get(key).getAsShort();
    }

    @Override
    public int getInt(String key) {
        return instance.get(key).getAsInt();
    }

    @Override
    public long getLong(String key) {
        return instance.get(key).getAsLong();
    }

    @Override
    public double getDouble(String key) {
        return instance.get(key).getAsDouble();
    }

    @Override
    public float getFloat(String key) {
        return instance.get(key).getAsFloat();
    }

    @Override
    public <T extends Enum<T>> Enum<T> getEnum(String key, Class<T> enumClass) {
        return Enum.valueOf(enumClass, getString(key));
    }

    @Override
    public void writeString(String key, String value) {
        instance.addProperty(key, value);
    }

    @Override
    public void writeBoolean(String key, boolean value) {
        instance.addProperty(key, value);
    }

    @Override
    public void writeByte(String key, byte value) {
        instance.addProperty(key, value);
    }

    @Override
    public void writeShort(String key, short value) {
        instance.addProperty(key, value);
    }

    @Override
    public void writeInt(String key, int value) {
        instance.addProperty(key, value);
    }

    @Override
    public void writeLong(String key, long value) {
        instance.addProperty(key, value);
    }

    @Override
    public void writeDouble(String key, double value) {
        instance.addProperty(key, value);
    }

    @Override
    public void writeFloat(String key, float value) {
        instance.addProperty(key, value);
    }

    @Override
    public <T extends Enum<T>> void writeEnum(String key, Enum<T> value) {
        instance.addProperty(key, value.name());
    }

    @Override
    public void remove(String key) {
        instance.remove(key);
    }

    @Override
    public JsonObject raw() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends StorageBuffer> T convertTo(Class<T> bufferClass) {
        StorageBuffer buffer;

        if(JsonBuffer.class.equals(bufferClass)) {
            return (T) new JsonBuffer(instance);
        } else if(MongoBuffer.class.equals(bufferClass)) {
            buffer = new MongoBuffer();
        } else if(MySQLBuffer.class.equals(bufferClass)) {
            buffer = new MySQLBuffer();
        } else if(XmlBuffer.class.equals(bufferClass)) {
            buffer = new XmlBuffer();
        } else if(YamlBuffer.class.equals(bufferClass)) {
            buffer = new YamlBuffer();
        } else {
            throw new InternalError("Unconvertable BufferClass: " + (bufferClass == null ? "null"
                    : bufferClass.getCanonicalName()));
        }

        writeToBuffer(buffer);

        return (T) buffer;
    }

    private void writeToBuffer(StorageBuffer buffer) {
        for (Map.Entry<String, JsonElement> keyEntry : instance.entrySet()) {
            if(!keyEntry.getValue().isJsonPrimitive()) {
                continue;
            }

            JsonPrimitive primitive = keyEntry.getValue().getAsJsonPrimitive();

            if(primitive.isString()) {
                buffer.writeString(keyEntry.getKey(), primitive.getAsString());
            } else if(primitive.isBoolean()) {
                buffer.writeBoolean(keyEntry.getKey(), primitive.getAsBoolean());
            } else if(primitive.isNumber()) {
                buffer.writeDouble(keyEntry.getKey(), primitive.getAsDouble());
            } else {
                throw new InternalError(); // WTF
            }
        }
    }
}
