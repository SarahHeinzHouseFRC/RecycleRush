package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ElevatorUpCommand extends Command
{
    public ElevatorUpCommand()
    {
        requires(Elevator.getInstance());
    }

    @Override
    protected void initialize()
    {
    }

    @Override
    protected void execute()
    {
        Elevator.getInstance().up();
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
    }

    @Override
    protected void interrupted()
    {
    }
}
