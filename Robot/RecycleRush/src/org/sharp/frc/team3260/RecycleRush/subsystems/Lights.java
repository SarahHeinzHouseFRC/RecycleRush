package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import org.sharp.frc.team3260.RecycleRush.Robot;

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

        i2c = new I2C(I2C.Port.kMXP, 168);
    }

    @Override
    protected void initDefaultCommand()
    {
    }

    // This routine sends up to 6 bytes to place in the Arduino's "read/write" array and
    // then reads it back into the public byte array "dataReceived" for verification
    private void arduinoWrite(byte newData[], byte length)
    {
        if(length > 6)
        {
            length = 6;
        }

        dataToSend[0] = 2;

        System.arraycopy(newData, 0, dataToSend, 1, length);

        if(!i2c.transaction(dataToSend, dataToSend.length, dataReceived, 0))
        {
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
//        else
//        {
//            log.error("Failure to send " + Arrays.toString(dataToSend) + " to Arduino.");
//        }
    }

    private void setLightMode(byte lightMode, byte[] additionalData)
    {
        byte[] writeData = concat(new byte[]{lightMode}, additionalData);

        arduinoWrite(writeData, (byte) writeData.length);
    }

    public void setLightMode(LightOption lightOption)
    {
        setLightMode(lightOption.getID(), lightOption.getAdditionalData());
    }

    public static class LightOption
    {
        public static final LightOption DEFAULT = new LightOption((byte) 0, "DEFAULT");
        public static final LightOption LOW_BATTERY = new LightOption((byte) 1, "LOW_BATTERY");
        public static final LightOption LOW_PRESSURE = new LightOption((byte) 2, "LOW_PRESSURE");
        public static final LightOption ELEVATOR_STATUS = new LightOption((byte) 3, "ELEVATOR_STATUS");
        public static final LightOption ALLIANCE_COLOR = new LightOption((byte) 4, "ALLIANCE_COLOR");
        public static final LightOption YOLO = new LightOption((byte) 5, "YOLO");
        public static final LightOption MATCH_READY = new LightOption((byte) 6, "MATCH_READY");

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
            resetAdditionalData();

            if(additionalData.length <= 5)
            {
                this.additionalData = additionalData;
            }
        }

        public void resetAdditionalData()
        {
            additionalData = new byte[]{};
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

    public void updateLights()
    {
        Lights.LightOption lightOption = Lights.LightOption.DEFAULT;

        double batteryVoltage = DriverStation.getInstance().getBatteryVoltage();

        double pressure = DriveTrain.getInstance().getPressure();

        byte elevatorPosition = Elevator.getInstance().getPositionAsByte();

        boolean gripperClosed = Gripper.getInstance().isClosed();

        double pitch = DriveTrain.getInstance().getIMU().getPitch();

        if(Robot.getInstance().isDisplayingMatchReady())
        {
            lightOption = LightOption.MATCH_READY;
        }
        else if(pitch > 40 || pitch < -40)
        {
            lightOption = LightOption.YOLO;
        }
        else if(batteryVoltage < 11)
        {
            double batteryPercent = (batteryVoltage / 13);

            byte batteryPercentByte = (byte) (batteryPercent * Byte.MAX_VALUE);

            lightOption = Lights.LightOption.LOW_BATTERY;
            lightOption.setAdditionalData(batteryPercentByte);
        }
        else if(pressure < 40)
        {
            byte pressureAsByte = (byte) ((DriverStation.getInstance().getBatteryVoltage() / 120) * Byte.MAX_VALUE);

            lightOption = Lights.LightOption.LOW_PRESSURE;
            lightOption.setAdditionalData(pressureAsByte);
        }
        else if(DriverStation.getInstance().isDisabled() && DriverStation.getInstance().isFMSAttached())
        {
            lightOption = LightOption.ALLIANCE_COLOR;
            lightOption.setAdditionalData((byte) (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red ? 0 : 1));
        }
        else if(elevatorPosition > 10 || gripperClosed)
        {
            lightOption = Lights.LightOption.ELEVATOR_STATUS;
            lightOption.setAdditionalData(Elevator.getInstance().getPositionAsByte());
        }

        Lights.getInstance().setLightMode(lightOption);
    }

    public static Lights getInstance()
    {
        return instance;
    }
}
