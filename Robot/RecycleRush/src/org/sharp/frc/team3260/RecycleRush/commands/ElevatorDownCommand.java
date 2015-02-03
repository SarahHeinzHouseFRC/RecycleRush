package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ElevatorDownCommand extends Command
{
    public ElevatorDownCommand()
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
        Elevator.getInstance().down();
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
