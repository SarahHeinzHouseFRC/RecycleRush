package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Arms;

public class CloseElevatorArmsCommand extends Command
{
    public CloseElevatorArmsCommand()
    {
        requires(Arms.getInstance());
    }

    @Override
    protected void initialize()
    {
        Arms.getInstance().closeElevatorArms();
    }

    @Override
    protected void execute()
    {
        setTimeout(0.25);
    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut();
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