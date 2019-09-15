package io.github.etrayed.icury.storage.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import org.bson.Document;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Etrayed
 */
public class MongoStorage implements Storage<MongoBuffer> {

    private Closeable closeable;

    private MongoCollection<Document> collection;

    @Override
    public void load() {
        this.closeable = new MongoClient(null, 0);

        MongoDatabase database = ((MongoClient) closeable).getDatabase(null);

        if(!database.listCollectionNames().into(new ArrayList<>()).contains("IcuryStorages")) {
            database.createCollection("IcuryStorages");
        }

        this.collection = database.getCollection("IcuryStorages");
    }

    @Override
    public void close() {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store(String key, MongoBuffer mongoBuffer) {
        collection.insertOne(mongoBuffer.raw().append("key", key));
    }

    @Override
    public MongoBuffer get(String key) {
        return new MongoBuffer(collection.find(Filters.eq("key", key)).first());
    }

    @Override
    public MongoBuffer delete(String key) {
        return new MongoBuffer(collection.findOneAndDelete(Filters.eq("key", key)));
    }

    @Override
    public String[] keys() {
        List<String> keyList = new ArrayList<>();

        for (Document document : collection.find()) {
            keyList.add(document.get("key", String.class));
        }

        return keyList.toArray(new String[keyList.size()]);
    }

    @Override
    public StorageType type() {
        return StorageType.MONGODB;
    }
}
