package io.github.etrayed.icury.storage;

import io.github.etrayed.icury.storage.json.JsonBuffer;
import io.github.etrayed.icury.storage.json.JsonStorage;
import io.github.etrayed.icury.storage.mongodb.MongoBuffer;
import io.github.etrayed.icury.storage.mongodb.MongoStorage;
import io.github.etrayed.icury.storage.mysql.MySQLBuffer;
import io.github.etrayed.icury.storage.mysql.MySQLStorage;
import io.github.etrayed.icury.storage.xml.XmlBuffer;
import io.github.etrayed.icury.storage.xml.XmlStorage;
import io.github.etrayed.icury.storage.yaml.YamlBuffer;
import io.github.etrayed.icury.storage.yaml.YamlStorage;

/**
 * @author Etrayed
 */
public class Storages {

    @SuppressWarnings("unchecked")
    public static void convertStorage(Storage input, Storage output) {
        Class<? extends StorageBuffer> storageBufferClass;

        if(input instanceof MongoStorage) {
            storageBufferClass = MongoBuffer.class;
        } else if(input instanceof JsonStorage) {
            storageBufferClass = JsonBuffer.class;
        } else if(input instanceof MySQLStorage) {
            storageBufferClass = MySQLBuffer.class;
        } else if(input instanceof XmlStorage) {
            storageBufferClass = XmlBuffer.class;
        } else if(input instanceof YamlStorage) {
            storageBufferClass = YamlBuffer.class;
        } else {
            throw new InternalError("Unconvertable Storage: " + (input == null ? "null" : input.getClass().getCanonicalName()));
        }

        for (String key : input.keys()) {
            output.store(key, input.get(key).convertTo(storageBufferClass));
        }
    }
}
