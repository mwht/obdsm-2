package ovh.spajste.yeti;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public abstract class Initialization {

    protected String protocolName;
    protected String protocolSelectSequence;

    public ArrayList<Byte> availablePIDS = new ArrayList<>();

    private byte[] initializeSequence = {0x01,0x00,0x0A}; // 0100\n
    private byte[] elmResetSequence = {0x41,0x54,0x20,0x5A,0x0A}; // AT Z\n
    private String elmIdentificator = "Unknown";
    private SerialCommunication serialCommunication;
    private SerialPort serialPort;

    Initialization(SerialPort serialPort, SerialCommunication serialCommunication)
    {
        protocolName = "Unknown";
        protocolSelectSequence = "Unknown";
        this.serialPort = serialPort;
        this.serialCommunication = serialCommunication;
    }

    public abstract void reciveProtocolSelectAnswer() throws InitializationException;
    public abstract void reciveInitailizationAnswer() throws InitializationException;

    private void extractAvailablePIDS(byte[] pids)
    {
        // TODO: finish this.
    }

    private void resetElm() throws SerialSendDataException, PortNotOpenException {
        serialCommunication.sendData(serialPort, elmResetSequence);
        byte[] elmAnswer = serialCommunication.waitAndReadData(serialPort);
        elmIdentificator = new String(elmAnswer);
    }

    void initializeProtocol() throws SerialSendDataException, PortNotOpenException, InitializationException {
        resetElm();
        serialCommunication.sendData(serialPort, protocolSelectSequence.getBytes());
        reciveProtocolSelectAnswer();
        serialCommunication.sendData(serialPort, initializeSequence);
        reciveInitailizationAnswer();
    }


}
