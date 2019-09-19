package io.github.etrayed.icury.storage.yaml;

import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Etrayed
 */
public class YamlStorage implements Storage<YamlBuffer> {

    private static final Path STORAGE_FILE_PATH = Paths.get("plugins/Icury/storage.yml");

    private static final Yaml YAML = new Yaml();

    private final Map<String, YamlBuffer> buffer = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void load() {
        try {
            if (Files.notExists(STORAGE_FILE_PATH)) {
                Files.createFile(STORAGE_FILE_PATH);
            }

            Map<String, Object> rawBuffer = (Map<String, Object>) YAML.load(Files.newBufferedReader(STORAGE_FILE_PATH,
                    StandardCharsets.UTF_8));

            rawBuffer.forEach(new BiConsumer<String, Object>() {

                @Override
                public void accept(String s, Object o) {
                    buffer.put(s, new YamlBuffer((Map<String, Object>) o));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {}

    @Override
    public void store(String key, YamlBuffer yamlBuffer) {
        buffer.put(key, yamlBuffer);

        save();
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(STORAGE_FILE_PATH)) {
            Map<String, Object> values = new HashMap<>();

            buffer.forEach(new BiConsumer<String, YamlBuffer>() {

                @Override
                public void accept(String s, YamlBuffer yamlBuffer) {
                    values.put(s, yamlBuffer.raw());
                }
            });

            YAML.dump(values, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public YamlBuffer get(String key) {
        return buffer.get(key);
    }

    @Override
    public YamlBuffer delete(String key) {
        YamlBuffer yamlBuffer = buffer.remove(key);

        save();

        return yamlBuffer;
    }

    @Override
    public String[] keys() {
        return buffer.keySet().toArray(new String[buffer.size()]);
    }

    @Override
    public StorageType type() {
        return StorageType.YAML;
    }
}
