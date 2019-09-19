package io.github.etrayed.icury.storage.mysql;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import io.github.etrayed.icury.Icury;
import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;
import io.github.etrayed.icury.util.ConfigurationInterpreter;

import javax.persistence.Column;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Etrayed
 */
public class MySQLStorage implements Storage<MySQLBuffer> {

    private Closeable connectionSource;

    private StorageDataAccessObject dataAccessObject;

    @Override
    public void load() {
        try {
            ConfigurationInterpreter.DatabaseCredentials credentials
                    = Icury.getConfigurationInterpreter().getDatabaseCredentials();

            Icury.getLogger().info("Connecting to MySQL Database...");

            long timestamp = System.currentTimeMillis();

            if(credentials.customUrl.isEmpty()) {
                Icury.getLogger().info("Using \"databaseCredentials\".");

                this.connectionSource = new JdbcConnectionSource("jdbc:mysql://" + credentials.hostname + ":"
                        + credentials.port + "/" + credentials.databaseName + "?autoReconnect=true",
                        credentials.username, credentials.password);
            } else {
                Icury.getLogger().info("Using \"customUrl\".");

                this.connectionSource = new JdbcConnectionSource(credentials.customUrl);
            }

            Icury.getLogger().info("Connection established! Took " + (System.currentTimeMillis() - timestamp)
                    + "ms. Full log can be found in \"plugins/Icury/sqlActions.log\".");

            Icury.getLogger().info("Creating table \"IcuryStorage\" if it not already exists.");

            TableUtils.createTableIfNotExists((ConnectionSource) connectionSource, BufferRepresentation.class);

            this.dataAccessObject = new StorageDataAccessObject(((ConnectionSource) connectionSource));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store(String key, MySQLBuffer mySQLBuffer) {
        try {
            dataAccessObject.createIfNotExists(new BufferRepresentation(key, BufferSerializer.serialize(mySQLBuffer)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MySQLBuffer get(String key) {
        try {
            return BufferSerializer.deserialize(dataAccessObject.queryForId(key).data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public MySQLBuffer delete(String key) {
        MySQLBuffer mySQLBuffer = get(key);

        try {
            dataAccessObject.deleteById(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mySQLBuffer;
    }

    @Override
    public String[] keys() {
        List<String> keys = new ArrayList<>();

        try {
            for (BufferRepresentation bufferRepresentation : dataAccessObject.queryForAll()) {
                keys.add(bufferRepresentation.key);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return keys.toArray(new String[keys.size()]);
    }

    @Override
    public StorageType type() {
        return StorageType.MYSQL;
    }

    @Override
    public void close() throws IOException {
        if(connectionSource != null) {
            connectionSource.close();

            Icury.getLogger().info("Closed database connection.");
        }
    }

    @DatabaseTable(tableName = "IcuryStorage", daoClass = StorageDataAccessObject.class)
    private static final class BufferRepresentation {

        @DatabaseField(id = true)
        private String key;

        @DatabaseField
        @Column(name = "BLOB")
        private String data;

        private BufferRepresentation() {}

        private BufferRepresentation(String key, String data) {
            this.key = key;
            this.data = data;
        }
    }

    public static final class StorageDataAccessObject extends BaseDaoImpl<BufferRepresentation, String> {

        public StorageDataAccessObject(ConnectionSource connectionSource) throws SQLException {
            super(connectionSource, BufferRepresentation.class);
        }
    }

    private static final class BufferSerializer {

        private static String serialize(MySQLBuffer buffer) {
            JsonObject baseObject = new JsonObject();

            buffer.raw().forEach(new BiConsumer<String, Object>() {

                @Override
                public void accept(String key, Object value) {
                    if(value instanceof Number) {
                        baseObject.addProperty(key, (Number) value);
                    } else if(value instanceof Boolean) {
                        baseObject.addProperty(key, (boolean) value);
                    } else {
                        baseObject.addProperty(key, value.toString());
                    }
                }
            });

            return baseObject.toString();
        }

        private static MySQLBuffer deserialize(String data) {
            Map<String, Object> keyValues = new HashMap<>();
            JsonObject baseObject = Icury.getInstance().getGson().fromJson(data, JsonObject.class);

            for (Map.Entry<String, JsonElement> elementEntry : baseObject.entrySet()) {
                JsonPrimitive primitiveValue = elementEntry.getValue().getAsJsonPrimitive();
                Object value;

                if(primitiveValue.isNumber()) {
                    value = primitiveValue.getAsNumber();
                } else if(primitiveValue.isBoolean()) {
                    value = primitiveValue.getAsBoolean();
                } else {
                    value = primitiveValue.getAsString();
                }

                keyValues.put(elementEntry.getKey(), value);
            }

            return new MySQLBuffer(keyValues);
        }
    }
}
