package com.radostyan.cordova.livepreview;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class MJpegInputStream extends DataInputStream {
    private final byte[] SOI_MARKER = {(byte) 0xFF, (byte) 0xD8};
    private final byte[] EOF_MARKER = {(byte) 0xFF, (byte) 0xD9};
    private final static String CONTENT_LENGTH = "Content-Length";
    private final static int HEADER_MAX_LENGTH = 100;
    private final static int FRAME_MAX_LENGTH = 40000 + HEADER_MAX_LENGTH;

    public MJpegInputStream(InputStream inputStream) {
        super(new BufferedInputStream(inputStream, FRAME_MAX_LENGTH));
    }

    private int getEndOfSequence(DataInputStream dataInputStream, byte[] sequence) throws IOException {
        int sequenceIndex = 0;
        byte readByteData;
        for(int index = 0; index < FRAME_MAX_LENGTH; index++) {
            readByteData = (byte) dataInputStream.readUnsignedByte();
            if(readByteData == sequence[sequenceIndex]) {
                sequenceIndex++;
                if(sequenceIndex == sequence.length) {
                    return index + 1;
                }
            } else {
                sequenceIndex = 0;
            }
        }
        return -1;
    }

    private int getStartOfSequence(DataInputStream dataInputStream, byte[] sequence) throws IOException {
        int endIndex = getEndOfSequence(dataInputStream, sequence);
        return (endIndex < 0) ? (-1) : (endIndex - sequence.length);
    }

    private int parseContentLength(byte[] headerByteData) throws IOException, NumberFormatException {
        ByteArrayInputStream bais = new ByteArrayInputStream(headerByteData);
        Properties properties = new Properties();
        properties.load(bais);
        return Integer.parseInt(properties.getProperty(CONTENT_LENGTH));
    }

    public String readMJpegFrame() throws IOException {
        mark(FRAME_MAX_LENGTH);
        int headerLength = getStartOfSequence(this, SOI_MARKER);
        int contentLength;
        reset();

        byte[] headerData = new byte[headerLength];
        readFully(headerData);
        try {
            contentLength = parseContentLength(headerData);
        } catch (NumberFormatException e) {
            e.getStackTrace();
            contentLength = getEndOfSequence(this, EOF_MARKER);
        }
        reset();

        byte[] frameData = new byte[contentLength];
        skipBytes(headerLength);
        readFully(frameData);

        return Base64.encodeToString(frameData, Base64.DEFAULT);
    }
}
