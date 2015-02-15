package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.I2C;

public class Lights extends SHARPSubsystem
{
    protected static Lights instance;

    protected I2C i2c;

    protected static byte dataReceived[] = {0, 0, 0, 0, 0, 0, 0};
    protected static byte dataToSend[] = {0, 0, 0, 0, 0, 0, 0};

    public Lights()
    {
        super("Lights");

        instance = this;

        i2c = new I2C(I2C.Port.kOnboard, 168);
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    // This routine retrieves the incrementing counter value from the Arduino and reconstitutes
    // it back into a 32 bit integer.  This is a rather convoluted way of doing it as JAVA does
    // not support an unsigned single byte (bytes are signed)
    private int arduinoCounter()
    {
        // Request Command #1 - Return Counter Value ("Register" 1)

        i2c.write(1, 0);

        // Read 5 bytes of data, with 0 bytes to send.  A false return value indicates success

        if (!i2c.transaction(dataToSend, 0, dataReceived, 5))
        {
            // If the data returned is indeed the counter, the first byte should be a 1 - identical
            // to the value we sent above

            if (dataReceived[0] != 1)
            {
                log.error("Invalid data returned from Arduino.");
            }
            else
            {
                return (((int) dataReceived[4] * 16777216) + (((int) dataReceived[3] & 0x000000ff) * 65536) + (((int) dataReceived[2] & 0x000000ff) * 256) + ((int) dataReceived[1] & 0x000000ff));
            }
        }
        else
        {
            log.error("Failure to read from Arduino.");
        }

        return 0;
    }


    // This routine sends up to 6 bytes to place in the Arduino's "read/write" array and
    // then reads it back into the public byte array "dataReceived" for verification
    private void arduinoWrite(byte newData[], byte length)
    {
        // Maximum 6 bytes to send in addition to the "command" byte.  Place all the data into
        // the byte array.
        if (length > 6)
        {
            length = 6;
        }

        dataToSend[0] = 2;

        for (int i = 0; i < length; i++)
        {
            dataToSend[i + 1] = newData[i];
        }

        // Send the data to the Arduino.  Do not request any return bytes or this function
        // will fail
        if (!i2c.transaction(dataToSend, length + 1, dataReceived, 0))
        {
            // After successfully sending the data, perform a data read.  Since the last
            // transaction was a write with a "Command" value of 2, the Arduino will assume
            // this is the data to return.

            if (!i2c.transaction(dataToSend, 0, dataReceived, 7))
            {
                if (dataReceived[0] != 2)
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
}