package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.commands.ElevatorHoldPositionCommand;

/**
 * TODO: Decide whether or not the Elevator gripper and Elevator should be in the same subsystem
 * TODO: Elevator sensors
 * TODO: Elevator control methods
 * TODO: Elevator operator interface
 * TODO: Elevator automation commands
 */
public class Elevator extends SHARPSubsystem
{
    protected static Elevator instance;

    private CANTalon elevatorTalon;

    public Elevator()
    {
        super("Elevator");

        instance = this;

        elevatorTalon = new CANTalon(Constants.elevatorTalonID.getInt());

        elevatorTalon.enableBrakeMode(true);

//        elevatorTalon.changeControlMode(CANTalon.ControlMode.Position);
//        elevatorTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    }

    public void up()
    {
        setElevator(1.0);
    }

    public void down()
    {
        setElevator(-1.0);
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

    @Override
    protected void log()
    {
        SmartDashboard.putNumber("Elevator Talon", elevatorTalon.get());
        SmartDashboard.putNumber("Elevator Encoder", elevatorTalon.getEncPosition());
    }

    public static Elevator getInstance()
    {
        if (instance == null)
        {
            System.out.println("Something has gone horribly wrong.");
        }

        return instance;
    }
}
