package de.sharpmind.serial;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by marco on 04.12.15.
 */
public class SMSerialPortTest {
    @Test
    public void testToString7E2() {
        SMSerialPort port = SMSerialPort.getPort_7E2("/dev/ttyACM9", 19200);

        String toString = port.toString();

        TestCase.assertEquals("SMSerialPort{/dev/ttyACM9-7E2}", toString);
    }

    @Test
    public void testToString8N1() {
        SMSerialPort port = SMSerialPort.getPort_8N1("/dev/ttyACM5", 14400);

        String toString = port.toString();

        TestCase.assertEquals("SMSerialPort{/dev/ttyACM5-8N1}", toString);
    }
}
