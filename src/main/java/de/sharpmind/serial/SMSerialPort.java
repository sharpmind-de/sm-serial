package de.sharpmind.serial;

import com.jcabi.log.Logger;
import gnu.io.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * Class for serial port
 */
public class SMSerialPort {
    private RXTXPort serial;
    private String port = null;
    private boolean connected = false;
    private String owner;

    // default settings are 115200, 8N1
    private int baud = 115200;
    private int databits = SerialPort.DATABITS_8;
    private int stopbits = SerialPort.STOPBITS_1;
    private int parity = SerialPort.PARITY_NONE;

    /**
     * Get the default serial port
     * - 8 databits
     * - no parity
     * - 1 stop bit
     *
     * @param port the port name
     * @return the serial port
     */
    public static SMSerialPort getPort_8N1(String port, int baud) {
        return new SMSerialPort(port, baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    /**
     * Get a serial port with
     * - 7 databits
     * - even parity
     * - 2 stop bits
     * <p/>
     * This is used for IBIS
     *
     * @param port the port name
     * @return the serial port
     */
    public static SMSerialPort getPort_7E2(String port, int baud) {
        return new SMSerialPort(port, baud, SerialPort.DATABITS_7, SerialPort.STOPBITS_2, SerialPort.PARITY_EVEN);
    }

    /**
     * Class Constructor for a NRSerialPort with a given port and baudrate.
     *
     * @param port the port to connect to (i.e. COM6 or /dev/ttyUSB0)
     * @param baud the baudrate to use (i.e. 9600 or 115200)
     */
    public SMSerialPort(String port, int baud, int databits, int stopbits, int parity) {
        this.port = port;
        setBaud(baud);
        this.databits = databits;
        this.stopbits = stopbits;
        this.parity = parity;

        this.owner = port;
    }


    public boolean connect() {
        if (isConnected()) {
            Logger.warn(this, port + " is already connected.");
            return true;
        }

        Logger.debug(this, "Connecting to port: " + port + " - owner: " + owner);

        try {
            RXTXPort comm;
            CommPortIdentifier ident;
            try {
                ident = CommPortIdentifier.getPortIdentifier(port);
            } catch (NoSuchPortException nspEx) {
                Logger.error(this, "No such port: " + port);
                return false;
            }

            try {
                comm = ident.open(owner, 5000);
            } catch (PortInUseException e) {
                Logger.error(this, "This is a bug, passed the ownership test above: ", e);
                return false;
            }

            if (comm == null) {
                throw new UnsupportedCommOperationException("Non-serial connections are unsupported.");
            }

            serial = comm;
            serial.enableReceiveTimeout(100);
            serial.setSerialPortParams(baud, databits, stopbits, parity);
            setConnected(true);
        } catch (NativeResourceException e) {
            throw new NativeResourceException(e.getMessage());
        } catch (Exception e) {
            Logger.error(this, "Failed to connect on port: " + port + " exception: ", e);
            setConnected(false);
        }

        if (isConnected()) {
            serial.notifyOnDataAvailable(true);
        }

        return isConnected();
    }

    public InputStream getInputStream() {
        return serial.getInputStream();
    }

    public OutputStream getOutputStream() {
        return serial.getOutputStream();
    }

    /**
     * Set the port to use (i.e. COM6 or /dev/ttyUSB0)
     *
     * @param port the serial port to use
     */
    private void setPort(String port) {
        this.port = port;
    }


    public void disconnect() {
        try {
            try {
                getInputStream().close();
                getOutputStream().close();
                serial.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            serial = null;
            setConnected(false);
        } catch (UnsatisfiedLinkError e) {
            throw new NativeResourceException(e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        if (this.connected == connected)
            return;
        this.connected = connected;
    }

    public void setBaud(int baud) {
        switch (baud) {
            case 1200:
            case 2400:
            case 4800:
            case 9600:
            case 14400:
            case 19200:
            case 28800:
            case 38400:
            case 57600:
            case 76800:
            case 115200:
            case 230400:
                this.baud = baud;
                return;
            default:
                throw new RuntimeException("Invalid baud rate! " + baud);
        }
    }

    public void addDataAvailableListener(SerialPortEventListener dataAvailableListener) throws TooManyListenersException {
        serial.addEventListener(dataAvailableListener);
        serial.notifyOnDataAvailable(true);
    }


    public void notifyOnDataAvailable(boolean b) {
        serial.notifyOnDataAvailable(b);
    }


    @Override
    public String toString() {
        String parString = null;

        switch (parity) {
            case SerialPort.PARITY_NONE:
                parString = "N";
                break;
            case SerialPort.PARITY_EVEN:
                parString = "E";
                break;
            case SerialPort.PARITY_MARK:
                parString = "M";
                break;
            case SerialPort.PARITY_ODD:
                parString = "O";
                break;
            case SerialPort.PARITY_SPACE:
                parString = "S";
                break;

        }

        return String.format("SMSerialPort{%s-%d%s%d}",
                port,
                databits,
                parString,
                stopbits);
    }

}
