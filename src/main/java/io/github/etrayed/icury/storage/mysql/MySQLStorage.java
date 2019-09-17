package io.github.etrayed.icury.storage.mysql;

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
import java.nio.charset.StandardCharsets;
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

            if(credentials.customUrl.isEmpty()) {
                this.connectionSource = new JdbcConnectionSource("jdbc:mysql://" + credentials.hostname + ":"
                        + credentials.port + "/" + credentials.databaseName + "?autoReconnect=true",
                        credentials.username, credentials.password);
            } else {
                this.connectionSource = new JdbcConnectionSource(credentials.customUrl);
            }

            TableUtils.createTableIfNotExists((ConnectionSource) connectionSource, BufferRepresentation.class);

            this.dataAccessObject = new StorageDataAccessObject(((ConnectionSource) connectionSource));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store(String key, MySQLBuffer mySQLBuffer) {
        try {
            dataAccessObject.create(new BufferRepresentation(key, BufferSerializer.serialize(mySQLBuffer)));
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

    private static final class StorageDataAccessObject extends BaseDaoImpl<BufferRepresentation, String> {

        private StorageDataAccessObject(ConnectionSource connectionSource) throws SQLException {
            super(connectionSource, BufferRepresentation.class);
        }
    }

    private static final class BufferSerializer {

        private static String serialize(MySQLBuffer buffer) {
            StringBuilder serializeBuilder = new StringBuilder();

            buffer.raw().forEach(new BiConsumer<String, Object>() {

                @Override
                public void accept(String key, Object value) {
                    serializeBuilder.append('|').append(key).append('_').append(objectToType(value)).append(':');

                    appendByteArray(serializeBuilder, value.toString().getBytes(StandardCharsets.UTF_8));
                }
            });

            if(serializeBuilder.length() > 0) {
                serializeBuilder.deleteCharAt(0);
            }

            return new String(serializeBuilder);
        }

        private static void appendByteArray(StringBuilder stringBuilder, byte[] array) {
            for (byte b : array) {
                stringBuilder.append(',');
                stringBuilder.append(b);
            }

            if(stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(0);
            }
        }

        private static MySQLBuffer deserialize(String data) {
            Map<String, Object> keyValues = new HashMap<>();

            for (String rawBufferValue : data.split("\\|")) {
                String[] keyTypeAndValue = rawBufferValue.split(":");
                String[] keyAndType = keyTypeAndValue[0].split("_");
                String decryptedValue = new String(parseByteArray(keyTypeAndValue[1]), StandardCharsets.UTF_8);

                if(keyAndType[1].equals("number")) {
                    keyValues.put(keyAndType[0], Double.parseDouble(decryptedValue));
                } else if(keyAndType[1].equals("boolean")) {
                    keyValues.put(keyAndType[0], Boolean.parseBoolean(decryptedValue));
                } else {
                    keyValues.put(keyAndType[0], decryptedValue);
                }
            }

            return new MySQLBuffer(keyValues);
        }

        private static byte[] parseByteArray(String input) {
            String[] values = input.split(",");
            byte[] bytes = new byte[values.length];

            for (int i = 0; i < values.length; i++) {
                bytes[i] = Byte.parseByte(values[i]);
            }

            return bytes;
        }

        private static String objectToType(Object value) {
            if(value instanceof Number) {
                return "number";
            } else if(value instanceof Boolean) {
                return "boolean";
            } else {
                return "string";
            }
        }
    }
}
