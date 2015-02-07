package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.commands.ElevatorHoldPositionCommand;

/**
 * TODO: Elevator sensors
 * TODO: Elevator control methods
 * TODO: Elevator operator interface
 * TODO: Elevator automation commands
 */
public class Elevator extends SHARPSubsystem
{
    protected static Elevator instance;

    private CANTalon elevatorTalon;

    private static final int ELEVATOR_TOLERANCE = 200;

    private boolean useEncoder;

    private int maxSpeedTicks = 50;

    public Elevator()
    {
        super("Elevator");

        instance = this;

        elevatorTalon = new CANTalon(Constants.elevatorTalonID.getInt());

        elevatorTalon.enableBrakeMode(true);

        elevatorTalon.changeControlMode(CANTalon.ControlMode.Position);

        elevatorTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

        elevatorTalon.setProfile(0);

        changeElevatorMode(false);
    }

    public void changeElevatorMode(boolean useEncoder)
    {
        this.useEncoder = useEncoder;

        if(!this.useEncoder)
        {
            elevatorTalon.disableControl();
        }
        else
        {
            elevatorTalon.enableControl();
        }
    }

    public void up(double speed)
    {
        if(useEncoder)
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
        if(useEncoder)
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
        elevatorTalon.set(setpoint.encoderValue);
    }

    public boolean atSetpoint()
    {
        return (!useEncoder || (Math.abs(elevatorTalon.getEncPosition() - elevatorTalon.getSetpoint()) < ELEVATOR_TOLERANCE));
    }

    public void stop()
    {
        setElevator(0.0);
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

    public static class ElevatorPosition
    {
        String positionName;
        int encoderValue;

        public static final ElevatorPosition GROUND = new ElevatorPosition("GROUND", 100);
        public static final ElevatorPosition TOP = new ElevatorPosition("TOP", 1000);

        public ElevatorPosition(String positionName, int encoderValue)
        {
            this.positionName = positionName;
            this.encoderValue = encoderValue;
        }
    }
}
