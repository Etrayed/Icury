package io.github.etrayed.icury.video;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.ByteBufferSeekableByteChannel;
import org.jcodec.common.io.SeekableByteChannel;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @author Etrayed
 */
public class Video {

    private final FrameGrab frameGrab;

    private Video(FrameGrab frameGrab) {
        this.frameGrab = frameGrab;
    }

    public static Video create(File file) throws IOException, JCodecException {
        return create(new FileInputStream(file));
    }

    public static Video create(InputStream inputStream) throws IOException, JCodecException {
        byte[] streamContent = new byte[inputStream.available()];

        inputStream.read(streamContent, 0, streamContent.length);
        inputStream.close();

        return create(new ByteBufferSeekableByteChannel(ByteBuffer.wrap(streamContent), streamContent.length));
    }

    public static Video create(SeekableByteChannel byteChannel) throws IOException, JCodecException {
        return new Video(FrameGrab.createFrameGrab(byteChannel));
    }
}