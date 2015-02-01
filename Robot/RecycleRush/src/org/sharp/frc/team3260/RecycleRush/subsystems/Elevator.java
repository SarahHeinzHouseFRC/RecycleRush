package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
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

    private CANTalon elevatorCIM;

    public Elevator()
    {
        super("Elevator");

        instance = this;

        elevatorCIM = new CANTalon(Constants.elevatorCIM.getInt());
    }

    public void up()
    {
        setElevator(0.5);
    }

    public void down()
    {
        setElevator(-0.5);
    }

    public void stop()
    {
        setElevator(0.0);
    }

    private void setElevator(double value)
    {
        elevatorCIM.set(value);
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new ElevatorHoldPositionCommand());
    }

    @Override
    protected void log()
    {

    }

    public static Elevator getInstance()
    {
        if (instance == null || instance.getClass() != Elevator.class)
        {
            System.out.println("Something has gone horribly wrong.");
        }

        return (Elevator) instance;
    }
}
