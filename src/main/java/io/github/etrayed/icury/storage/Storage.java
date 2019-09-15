package io.github.etrayed.icury.storage;

import java.io.Closeable;

/**
 * @author Etrayed
 */
public interface Storage<SB extends StorageBuffer> extends Closeable {

    void load();

    void store(String key, SB sb);

    SB get(String key);

    SB delete(String key);

    String[] keys();

    StorageType type();
}
