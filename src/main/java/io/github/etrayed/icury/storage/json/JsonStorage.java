package io.github.etrayed.icury.storage.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.etrayed.icury.Icury;
import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Etrayed
 */
public class JsonStorage implements Storage<JsonBuffer> {

    private static final Path STORAGE_FILE_PATH = Paths.get("plugins/Icury/storage.json");

    private final Map<String, JsonBuffer> buffer = new HashMap<>();

    @Override
    public void load() {
        try {
            if(Files.notExists(STORAGE_FILE_PATH)) {
                Files.write(STORAGE_FILE_PATH, new byte[] {123, 125}, StandardOpenOption.CREATE_NEW);
            }

            JsonObject baseObject = new JsonParser().parse(Files.newBufferedReader(STORAGE_FILE_PATH)).getAsJsonObject();

            for (Map.Entry<String, JsonElement> bufferEntry : baseObject.entrySet()) {
                buffer.put(bufferEntry.getKey(), new JsonBuffer(bufferEntry.getValue().getAsJsonObject()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {}

    @Override
    public void store(String key, JsonBuffer jsonBuffer) {
        buffer.put(key, jsonBuffer);

        save();
    }

    private void save() {
        try(BufferedWriter writer = Files.newBufferedWriter(STORAGE_FILE_PATH)) {
            JsonObject jsonObject = new JsonObject();

            buffer.forEach(new BiConsumer<String, JsonBuffer>() {

                @Override
                public void accept(String s, JsonBuffer jsonBuffer) {
                    jsonObject.add(s, jsonBuffer.raw());
                }
            });

            Icury.getInstance().getGson().toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JsonBuffer get(String key) {
        return buffer.get(key);
    }

    @Override
    public JsonBuffer delete(String key) {
        JsonBuffer jsonBuffer = buffer.remove(key);

        save();

        return jsonBuffer;
    }

    @Override
    public String[] keys() {
        return buffer.keySet().toArray(new String[buffer.size()]);
    }

    @Override
    public StorageType type() {
        return StorageType.JSON;
    }
}
