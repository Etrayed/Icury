package io.github.etrayed.icury.storage.yaml;

import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import java.io.IOException;

/**
 * @author Etrayed
 */
public class YamlStorage implements Storage<YamlBuffer> {

    @Override
    public void load() {

    }

    @Override
    public void store(String key, YamlBuffer yamlBuffer) {

    }

    @Override
    public YamlBuffer get(String key) {
        return null;
    }

    @Override
    public YamlBuffer delete(String key) {
        return null;
    }

    @Override
    public String[] keys() {
        return new String[0];
    }

    @Override
    public StorageType type() {
        return StorageType.YAML;
    }

    @Override
    public void close() throws IOException {

    }
}
