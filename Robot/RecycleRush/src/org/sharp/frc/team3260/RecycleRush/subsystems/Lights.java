package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.I2C;
import org.sharp.frc.team3260.RecycleRush.commands.UpdateLightsCommand;

public class Lights extends SHARPSubsystem
{
    protected static Lights instance;

    protected I2C i2c;

    protected static byte dataReceived[] = {0, 0, 0, 0, 0, 0, 0};
    protected static byte dataToSend[] = {0, 0, 0, 0, 0, 0, 0};
    private byte[] additionalData = {0, 0, 0, 0, 0};

    public Lights()
    {
        super("Lights");

        instance = this;

        i2c = new I2C(I2C.Port.kOnboard, 168);
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new UpdateLightsCommand());
    }

    // This routine sends up to 6 bytes to place in the Arduino's "read/write" array and
    // then reads it back into the public byte array "dataReceived" for verification
    private void arduinoWrite(byte newData[], byte length)
    {
        // Maximum 6 bytes to send in addition to the "command" byte.  Place all the data into
        // the byte array.
        if(length > 6)
        {
            length = 6;
        }

        dataToSend[0] = 2;

        System.arraycopy(newData, 0, dataToSend, 1, length);

        if(!i2c.transaction(dataToSend, length + 1, dataReceived, 0))
        {
            // After successfully sending the data, perform a data read.  Since the last
            // transaction was a write with a "Command" value of 2, the Arduino will assume
            // this is the data to return.
            if(!i2c.transaction(dataToSend, 0, dataReceived, 7))
            {
                if(dataReceived[0] != 2)
                {
                    log.error("Invalid data returned from Arduino.");
                }
            }
            else
            {
                log.error("Failure to read from Arduino.");
            }
        }
        else
        {
            log.error("Failure to send data to Arduino.");
        }
    }

    private void setLightMode(byte lightMode, byte[] additionalData)
    {
        this.additionalData = additionalData;

        byte[] writeData = concat(new byte[]{lightMode}, additionalData);

        arduinoWrite(writeData, (byte) writeData.length);
    }

    public void setLightMode(LightOption lightOption)
    {
        setLightMode(lightOption.getID(), additionalData);
    }

    public static class LightOption
    {
        public static final LightOption DEFAULT = new LightOption((byte) 0, "DEFAULT");
        public static final LightOption LOW_BATTERY = new LightOption((byte) 1, "LOW_BATTERY");
        public static final LightOption LOW_PRESSURE = new LightOption((byte) 2, "LOW_PRESSURE");
        public static final LightOption ELEVATOR_STATUS = new LightOption((byte) 3, "ELEVATOR_STATUS");

        private String name;
        private byte id;

        private byte[] additionalData;

        public LightOption(byte id, String name)
        {
            this.name = name;
            this.id = id;

            resetAdditionalData();
        }

        public void setAdditionalData(byte... additionalData)
        {
            if(additionalData.length <= 6)
            {
                resetAdditionalData();

                this.additionalData = additionalData;
            }
        }

        public void resetAdditionalData()
        {
            additionalData = new byte[] { };
        }

        public byte[] getAdditionalData()
        {
            return additionalData;
        }

        public byte getID()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }
    }

    public byte[] concat(byte[] a, byte[] b)
    {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static Lights getInstance()
    {
        return instance;
    }
}