package de.sharpmind.serial.util;

import gnu.io.CommPortIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Additional help to handle serial connect / disconnect, and registering
 * interrupt handler
 */
public class SerialHelper {
    /**
     * Retrieve the available serial port names.
     * <p/>
     *
     * @return Array of string for the available serial port names
     */
    @NotNull
    public static List<String> getSerialPortsNames() {
        List<String> portNames = new ArrayList<>();

        for (CommPortIdentifier identifier : getSerialPorts()) {
            portNames.add(identifier.getName());
        }

        return portNames;
    }

    /**
     * Retrieve the available serial ports.
     * <p/>
     *
     * @return Array of string for the available serial port names
     */
    @NotNull
    public static List<CommPortIdentifier> getSerialPorts() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        List<CommPortIdentifier> portList = new ArrayList<>();

        while (ports.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portList.add(port);
            }
        }
        return portList;
    }


}