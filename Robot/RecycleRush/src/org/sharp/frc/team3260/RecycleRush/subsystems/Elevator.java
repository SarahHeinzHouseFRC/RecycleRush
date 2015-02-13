package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.commands.ElevatorHoldPositionCommand;

import java.util.HashMap;

/**
 * TODO: Elevator sensors
 * TODO: Elevator control methods
 * TODO: Elevator operator interface
 * TODO: Elevator automation commands
 */
public class Elevator extends SHARPSubsystem
{
    private static final int ELEVATOR_TOLERANCE = 100;

    protected static Elevator instance;

    private CANTalon elevatorTalon;

    private boolean useEncoder = false;

    private int maxSpeedTicks = 50;

    public Elevator()
    {
        super("Elevator");

        instance = this;

        elevatorTalon = new CANTalon(Constants.elevatorTalonID.getInt());

        elevatorTalon.enableBrakeMode(true);

        elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);

        elevatorTalon.set(0.0);

        elevatorTalon.reverseOutput(true);
        //
        //        changeElevatorMode(false);
    }

    public void changeElevatorMode(boolean useEncoder)
    {
        this.useEncoder = useEncoder;

        if (!this.useEncoder)
        {
            log.warn("Disabling Elevator PID controller.");

            elevatorTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);

            elevatorTalon.set(0.0);

            elevatorTalon.disableControl();
        }
        else
        {
            log.info("Enabling Elevator PID controller.");

            elevatorTalon.changeControlMode(CANTalon.ControlMode.Position);

            elevatorTalon.set(0.0);

            elevatorTalon.set(elevatorTalon.getPosition());

            elevatorTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

            elevatorTalon.setProfile(0);

            elevatorTalon.enableControl();

        }
    }

    public void up(double speed)
    {
        if (useEncoder)
        {
            setElevator(elevatorTalon.getPosition() + (speed * maxSpeedTicks));
        }
        else
        {
            setElevator(speed);
        }
    }

    public void down(double speed)
    {
        if (useEncoder)
        {
            setElevator(elevatorTalon.getPosition() - (speed * maxSpeedTicks));
        }
        else
        {
            setElevator(-speed);
        }
    }

    public void setElevator(int setpoint)
    {
        if (setpoint < ElevatorPosition.GROUND.encoderValue)
        {
            setpoint = ElevatorPosition.GROUND.encoderValue;
        }
        else if (setpoint > ElevatorPosition.TOP.encoderValue)
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
        return (!useEncoder || (Math.abs(elevatorTalon.getEncPosition() - elevatorTalon.getSetpoint()) < ELEVATOR_TOLERANCE));
    }

    public void stop()
    {
        up(0.0);
    }

    private void setElevator(double value)
    {
        elevatorTalon.set(value);
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new ElevatorHoldPositionCommand());
    }

    public static Elevator getInstance()
    {
        if (instance == null)
        {
            Robot.getInstance().getLogger().error("Something has gone horribly wrong in " + Elevator.class.getSimpleName());
        }

        return instance;
    }

    public int getPosition()
    {
        return elevatorTalon.getEncPosition();
    }

    public static class ElevatorPosition
    {
        private static final HashMap<Integer, ElevatorPosition> positions = new HashMap<>();

        public String positionName;
        public int encoderValue;

        public static final ElevatorPosition GROUND = new ElevatorPosition(0, "GROUND", 100);
        public static final ElevatorPosition ONE_TOTE = new ElevatorPosition(1, "ONE_TOTE", 100);
        public static final ElevatorPosition TWO_TOTE = new ElevatorPosition(2, "TWO_TOTES", 100);
        public static final ElevatorPosition RECYCLING_CAN = new ElevatorPosition(3, "RECYCLING_CAN", 100);
        public static final ElevatorPosition THREE_TOTES = new ElevatorPosition(4, "THREE_TOTES", 100);
        public static final ElevatorPosition TOP = new ElevatorPosition(5, "TOP", 1000);

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

        public ElevatorPosition getPositionByName(String name)
        {
            for (Object obj : positions.entrySet())
            {
                if (obj.getClass().equals(ElevatorPosition.class))
                {
                    if (((ElevatorPosition) obj).positionName.equals(name))
                    {
                        return (ElevatorPosition) obj;
                    }
                }
            }

            return null;
        }

        public ElevatorPosition getPositionByIndex(int index)
        {
            if (positions.containsKey(index))
            {
                return positions.get(index);
            }

            return null;
        }
    }
}
