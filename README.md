# sm-serial
Java serial port library

Wrapper library around nrjavaserial that provides convenience methods to create correct serial port objects. Also supports more boud rates than nrjavaserial, i.e. 9600 Baud which is necessary for IBIS bus serial port connections.

Example usage:

Get a serial port connection suitable for IBIS bus:

```
SMSerialPort serialPort = SMSerialPort.getPort_7E2("/dev/ttyS0", 9600);
```

Get a serial connection to a modem:

```
private SMSerialPort serialPort;
private BufferedReader modemReader;
private BufferedWriter modemWriter;

public boolean connect() {
	if (serialPort != null && serialPort.isConnected()) return true;

	serialPort = SMSerialPort.getPort_8N1("/dev/modem", 57600);

	if (serialPort.connect() {
		this.modemReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		this.modemWriter = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));

		serialPort.addDataAvailableListener(this);
	}

	return serialPort.isConnected();
}
```
