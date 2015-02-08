package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ElevatorToSetpointCommand extends Command
{
    Elevator.ElevatorPosition setpoint;

    public ElevatorToSetpointCommand(Elevator.ElevatorPosition setpoint, double timeout)
    {
        requires(Elevator.getInstance());

        setTimeout(timeout);

        this.setpoint = setpoint;
    }

    @Override
    protected void initialize()
    {
        Elevator.getInstance().setElevator(setpoint);
    }

    @Override
    protected void execute()
    {
    }

    @Override
    protected boolean isFinished()
    {

        return isTimedOut() || Elevator.getInstance().atSetpoint();
    }

    @Override
    protected void end()
    {
        if (isTimedOut())
        {
            Elevator.getInstance().getLogger().severe("Elevator timed out en route to " + setpoint.positionName + ".");
        }
        else
        {
            Elevator.getInstance().getLogger().info("Reached " + setpoint.positionName + " at position " + setpoint.encoderValue);
        }
    }

    @Override
    protected void interrupted()
    {
        Elevator.getInstance().getLogger().warn("Interrupted while moving to " + setpoint.positionName + ", current position is " + Elevator.getInstance().getPosition());
    }
}
