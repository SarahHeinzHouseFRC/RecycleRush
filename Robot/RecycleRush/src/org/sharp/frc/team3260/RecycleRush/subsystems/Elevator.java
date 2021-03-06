package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.commands.ElevatorWithJoystickCommand;

import java.util.HashMap;

public class Elevator extends SHARPSubsystem
{
    protected static Elevator instance;

    private CANTalon elevatorTalon;

    private boolean useEncoder = false;

    public Elevator()
    {
        super("Elevator");

        instance = this;

        elevatorTalon = new CANTalon(Constants.elevatorTalonID.getInt(), 5);

        elevatorTalon.enableBrakeMode(true);

        elevatorTalon.reverseOutput(false);

        elevatorTalon.reverseSensor(true);

        elevatorTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

        elevatorTalon.setProfile(1);

        elevatorTalon.setPID(Constants.elevatorPositionControllerP.getDouble(), Constants.elevatorPositionControllerI.getDouble(), Constants.elevatorPositionControllerD.getDouble());

        elevatorTalon.setReverseSoftLimit(ElevatorPosition.GROUND.encoderValue);

        elevatorTalon.setForwardSoftLimit(ElevatorPosition.TOP.encoderValue);

        changeElevatorMode(false);
    }

    public void changeElevatorMode(boolean useEncoder)
    {
        if(this.useEncoder == useEncoder && ((useEncoder && getControlMode() == CANTalon.ControlMode.Position) || (!useEncoder && getControlMode() == CANTalon.ControlMode.PercentVbus)))
        {
            return;
        }

        this.useEncoder = useEncoder;

        if(!this.useEncoder)
        {
            log.warn("Disabling Elevator PID controller.");

            elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);

            elevatorTalon.set(0.0);

            elevatorTalon.enableControl();
        }
        else
        {
            log.info("Enabling Elevator PID controller.");

            elevatorTalon.changeControlMode(CANTalon.ControlMode.Position);

            elevatorTalon.set(0.0);

            elevatorTalon.setProfile(0);

            elevatorTalon.setPID(1.2, 0.0, 0.01);

            elevatorTalon.enableControl();

        }
    }

    public void up(double speed)
    {
        setElevator(speed);
    }

    public void down(double speed)
    {
        setElevator(-speed);
    }

    public void setElevator(int setpoint)
    {
        if(setpoint < ElevatorPosition.GROUND.encoderValue)
        {
            setpoint = ElevatorPosition.GROUND.encoderValue;
        }
        else if(setpoint > ElevatorPosition.TOP.encoderValue)
        {
            setpoint = ElevatorPosition.TOP.encoderValue;
        }

        elevatorTalon.set(setpoint);
    }

    public void setElevator(ElevatorPosition setpoint)
    {
        log.info("Received new setpoint " + setpoint.positionName + " with encoder value " + setpoint.encoderValue);

        elevatorTalon.set(setpoint.encoderValue);
    }

    public boolean atSetpoint()
    {
        return (!useEncoder || (Math.abs(elevatorTalon.getPosition() - elevatorTalon.getSetpoint()) < Constants.elevatorControllerOnTargetThreshold.getInt()) || (elevatorTalon.getSetpoint() < 0 && elevatorTalon.isRevLimitSwitchClosed()));
    }

    public void stop()
    {
        if(getControlMode() == CANTalon.ControlMode.PercentVbus)
        {
            up(0.0);
        }
        else
        {
            setElevator(elevatorTalon.getPosition());
        }
    }

    private void setElevator(double value)
    {
        elevatorTalon.set(value);
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new ElevatorWithJoystickCommand());
    }

    public static Elevator getInstance()
    {
        if(instance == null)
        {
            Robot.getInstance().getLogger().error("Something has gone horribly wrong in " + Elevator.class.getSimpleName());
        }

        return instance;
    }

    public int getPosition()
    {
        return -elevatorTalon.getEncPosition();
    }

    public byte getPositionAsByte()
    {
        double percentOfMax = getPosition() / ElevatorPosition.TOP.encoderValue;

        if(percentOfMax < 0)
        {
            percentOfMax = 0;
        }
        else if(percentOfMax > 1)
        {
            percentOfMax = 1;
        }

        return ((byte) (Byte.MAX_VALUE * percentOfMax));
    }

    public void setZero()
    {
        setElevatorPosition(0);
    }

    public CANTalon.ControlMode getControlMode()
    {
        return elevatorTalon.getControlMode();
    }

    public CANTalon getTalon()
    {
        return elevatorTalon;
    }

    public void setElevatorPosition(int elevatorPosition)
    {
        log.info("Elevator encoder value changed to " + elevatorPosition + ".");

        elevatorTalon.setPosition(elevatorPosition);
    }

    public static class ElevatorPosition
    {
        private static final HashMap<Integer, ElevatorPosition> positions = new HashMap<>();

        public String positionName;
        public int encoderValue;

        public static final ElevatorPosition GROUND = new ElevatorPosition(0, "GROUND", -250);
        public static final ElevatorPosition TWO_TOTE = new ElevatorPosition(2, "TWO_TOTES", 1270);
        public static final ElevatorPosition RECYCLING_CAN = new ElevatorPosition(3, "RECYCLING_CAN", 1150);
        public static final ElevatorPosition THREE_TOTES = new ElevatorPosition(4, "THREE_TOTES", 2500);
        public static final ElevatorPosition TOP = new ElevatorPosition(5, "TOP", 6200);
        public static final ElevatorPosition CAN_ON_TOTE = new ElevatorPosition(6, "CAN_ON_TOTE", 2260);
        public static final ElevatorPosition CAN_ABOVE_TOTE = new ElevatorPosition(7, "CAN_ABOVE_TOTE", 3000);
        public static final ElevatorPosition DRIVING_HEIGHT = new ElevatorPosition(8, "DRIVING_HEIGHT", 500);
        public static final ElevatorPosition LOADING_HEIGHT = new ElevatorPosition(9, "LOADING_HEIGHT", 2850);

        public ElevatorPosition(int index, String positionName, int encoderValue)
        {
            this.positionName = positionName;
            this.encoderValue = encoderValue;

            positions.put(index, this);
        }

        public ElevatorPosition(String positionName, int encoderValue)
        {
            this.positionName = positionName;
            this.encoderValue = encoderValue;
        }

        static public ElevatorPosition getPositionByIndex(int index)
        {
            if(positions.containsKey(index))
            {
                return positions.get(index);
            }

            return null;
        }
    }
}
