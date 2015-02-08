package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ElevatorHoldPositionCommand extends Command
{
    public ElevatorHoldPositionCommand()
    {
        requires(Elevator.getInstance());
    }

    @Override
    protected void initialize()
    {
        Elevator.getInstance().stop();
    }

    @Override
    protected void execute()
    {
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
