package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ElevatorToSetpointCommand extends Command
{
    Elevator.ElevatorPosition setpoint;

    public ElevatorToSetpointCommand(Elevator.ElevatorPosition setpoint)
    {
        requires(Elevator.getInstance());

        this.setpoint = setpoint;
    }

    public ElevatorToSetpointCommand(String positionName, int positionTicks)
    {
        requires(Elevator.getInstance());

        this.setpoint = new Elevator.ElevatorPosition(positionName, positionTicks);
    }

    @Override
    protected void initialize()
    {
        Elevator.getInstance().changeElevatorMode(true);

        Elevator.getInstance().setElevator(setpoint);

        int timeout = Math.abs(Elevator.getInstance().getPosition() - setpoint.encoderValue) / 1500;

        Elevator.getInstance().getLogger().info("Elevator To Setpoint Timeout: " + timeout);

        setTimeout(timeout);
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
        if(isTimedOut())
        {
            Elevator.getInstance().getLogger().warn("Elevator timed out en route to " + setpoint.positionName + ".");
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
