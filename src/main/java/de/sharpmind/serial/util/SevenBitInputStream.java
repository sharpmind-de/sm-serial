package de.sharpmind.serial.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream to read 7bit data
 *
 * Wrap the serial port input stream like this:
 *
 * InputStream in = new SevenBitInputStream(smSerialPort.getInputStream())
 */
public class SevenBitInputStream extends InputStream {
    private InputStream inputStream;

    public SevenBitInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        int value = inputStream.read();
        if (value == -1) return -1;

        // mask with 0x7F to get the 7bit data
        return value & 0x7F;
    }

    @Override
    public int read(byte[] b) throws IOException {
        byte[] temp = new byte[b.length];

        int read = inputStream.read(temp);

        // don't have the high order bit set
        for (int i = 0; i < read; i++) {
            b[i] = (byte) (temp[i] & 0x7F);
        }

        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        // todo - 7bit handling
        return inputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }
}
