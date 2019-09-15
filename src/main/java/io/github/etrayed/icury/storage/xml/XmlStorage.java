package io.github.etrayed.icury.storage.xml;

import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import java.io.IOException;

/**
 * @author Etrayed
 */
public class XmlStorage implements Storage<XmlBuffer> {

    @Override
    public void load() {

    }

    @Override
    public void store(String key, XmlBuffer xmlBuffer) {

    }

    @Override
    public XmlBuffer get(String key) {
        return null;
    }

    @Override
    public XmlBuffer delete(String key) {
        return null;
    }

    @Override
    public String[] keys() {
        return new String[0];
    }

    @Override
    public StorageType type() {
        return StorageType.XML;
    }

    @Override
    public void close() throws IOException {

    }
}
