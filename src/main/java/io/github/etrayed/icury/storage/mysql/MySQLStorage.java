package io.github.etrayed.icury.storage.mysql;

import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import java.io.IOException;

/**
 * @author Etrayed
 */
public class MySQLStorage implements Storage<MySQLBuffer> {

    @Override
    public void load() {

    }

    @Override
    public void store(String key, MySQLBuffer mySQLBuffer) {

    }

    @Override
    public MySQLBuffer get(String key) {
        return null;
    }

    @Override
    public MySQLBuffer delete(String key) {
        return null;
    }

    @Override
    public String[] keys() {
        return new String[0];
    }

    @Override
    public StorageType type() {
        return StorageType.MYSQL;
    }

    @Override
    public void close() throws IOException {

    }
}
