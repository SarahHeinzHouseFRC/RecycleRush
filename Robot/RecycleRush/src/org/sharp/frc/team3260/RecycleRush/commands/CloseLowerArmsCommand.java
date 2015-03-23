package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Arms;

public class CloseLowerArmsCommand extends Command
{
    public CloseLowerArmsCommand()
    {
        requires(Arms.getInstance());
    }

    @Override
    protected void initialize()
    {
        setTimeout(0.25);

        Arms.getInstance().closeLowerArms();
    }

    @Override
    protected void execute()
    {
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