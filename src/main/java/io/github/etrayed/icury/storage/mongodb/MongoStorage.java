package io.github.etrayed.icury.storage.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.mongodb.diagnostics.logging.Loggers;
import io.github.etrayed.icury.Icury;
import io.github.etrayed.icury.IcuryPlugin;
import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import io.github.etrayed.icury.util.ConfigurationInterpreter;
import org.bson.Document;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * @author Etrayed
 */
public class MongoStorage implements Storage<MongoBuffer> {

    private Closeable closeable;

    private MongoCollection<Document> collection;

    @SuppressWarnings("deprecation")
    @Override
    public void load() {
        ConfigurationInterpreter.DatabaseCredentials credentials
                = Icury.getConfigurationInterpreter().getDatabaseCredentials();

        Icury.getLogger().info("Connecting to Mongo Database...");

        long timestamp = System.currentTimeMillis();

        if(credentials.customUrl.isEmpty()) {
            Icury.getLogger().info("Using \"databaseCredentials\".");

            this.closeable = new MongoClient(new ServerAddress(credentials.hostname, credentials.port),
                    Collections.singletonList(MongoCredential.createCredential(credentials.username,
                            credentials.databaseName, credentials.password.toCharArray())));
        } else {
            Icury.getLogger().info("Using \"customUrl\".");

            this.closeable = new MongoClient(new MongoClientURI(credentials.customUrl));
        }

        Icury.getLogger().info("Connection established. Took " + (System.currentTimeMillis() - timestamp) + "ms.");

        MongoDatabase database = ((MongoClient) closeable).getDatabase(credentials.databaseName);

        if(!database.listCollectionNames().into(new ArrayList<>()).contains("IcuryStorage")) {
            database.createCollection("IcuryStorage");

            Icury.getLogger().info("Created collection \"IcuryStorage\".");
        }

        this.collection = database.getCollection("IcuryStorage");
    }

    @Override
    public void close() throws IOException {
        if(closeable != null) {
            closeable.close();

            Icury.getLogger().info("Closed database connection.");
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
