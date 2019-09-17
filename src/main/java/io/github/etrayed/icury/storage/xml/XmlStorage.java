package io.github.etrayed.icury.storage.xml;

import com.sun.xml.internal.fastinfoset.stax.events.*;

import io.github.etrayed.icury.storage.Storage;
import io.github.etrayed.icury.storage.StorageType;

import javafx.util.Pair;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
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
public class XmlStorage implements Storage<XmlBuffer> {

    private static final Path STORAGE_FILE_PATH = Paths.get("plugins/Icury/storage.xml");

    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();

    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newInstance();

    private final Map<String, XmlBuffer> buffer = new HashMap<>();

    @Override
    public void load() {
        try {
            if(Files.notExists(STORAGE_FILE_PATH)) {
                Files.createFile(STORAGE_FILE_PATH);
            }

            XMLEventReader reader = INPUT_FACTORY.createXMLEventReader(Files.newInputStream(STORAGE_FILE_PATH),
                    "UTF-8");

            reader.nextEvent();

            while (reader.hasNext()) {
                if(reader.peek() instanceof EndDocument) {
                    break;
                }

                Pair<String, XmlBuffer> bufferPair = readBuffer(reader);

                buffer.put(bufferPair.getKey(), bufferPair.getValue());
            }

            reader.close();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private Pair<String, XmlBuffer> readBuffer(XMLEventReader reader) throws XMLStreamException {
        String bufferKey = reader.nextEvent().asStartElement().getName().getLocalPart();
        Map<String, Object> keyValues = new HashMap<>();

        while (reader.hasNext()) {
            if(reader.peek() instanceof EndElement) {
                break;
            }

            StartElement startElement = reader.nextEvent().asStartElement();
            String key = startElement.getName().getLocalPart();
            String value = reader.nextEvent().asCharacters().getData();

            switch (startElement.getAttributeByName(new QName("type")).getValue()) {

                case "number":
                    keyValues.put(key, Double.parseDouble(value));
                    break;

                case "boolean":
                    keyValues.put(key, Boolean.parseBoolean(value));
                    break;

                default:
                    keyValues.put(key, value);
                    break;
            }

            reader.nextEvent();
        }

        reader.nextEvent();

        return new Pair<>(bufferKey, new XmlBuffer(keyValues));
    }

    @Override
    public void close() {}

    @Override
    public void store(String key, XmlBuffer xmlBuffer) {
        buffer.put(key, xmlBuffer);

        save();
    }

    private void save() {
        try {
            XMLEventWriter writer = OUTPUT_FACTORY.createXMLEventWriter(Files.newBufferedWriter(STORAGE_FILE_PATH,
                    StandardCharsets.UTF_8));

            writer.add(new StartDocumentEvent("UTF-8", "1.0"));

            buffer.forEach(new BiConsumer<String, XmlBuffer>() {

                @Override
                public void accept(String key, XmlBuffer xmlBuffer) {

                    try {
                        writer.add(new StartElementEvent(new QName(key)));

                        appendBufferValue(writer, xmlBuffer);

                        writer.add(new EndElementEvent());
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    }
                }
            });

            writer.add(new EndDocumentEvent());
            writer.close();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void appendBufferValue(XMLEventWriter writer, XmlBuffer buffer) {
        buffer.raw().forEach(new BiConsumer<String, Object>() {

            @Override
            public void accept(String key, Object value) {
                try {
                    StartElementEvent startElement = new StartElementEvent(new QName(key));

                    startElement.addAttribute(new AttributeBase("type", typeByObject(value)));

                    writer.add(startElement);
                    writer.add(new CharactersEvent(value.toString()));
                    writer.add(new EndElementEvent());
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String typeByObject(Object value) {
        if(value instanceof Number) {
            return "number";
        } else if(value instanceof Boolean) {
            return "boolean";
        } else {
            return "string";
        }
    }

    @Override
    public XmlBuffer get(String key) {
        return buffer.get(key);
    }

    @Override
    public XmlBuffer delete(String key) {
        XmlBuffer xmlBuffer = buffer.remove(key);

        save();

        return xmlBuffer;
    }

    @Override
    public String[] keys() {
        return buffer.keySet().toArray(new String[buffer.size()]);
    }

    @Override
    public StorageType type() {
        return StorageType.XML;
    }
}
