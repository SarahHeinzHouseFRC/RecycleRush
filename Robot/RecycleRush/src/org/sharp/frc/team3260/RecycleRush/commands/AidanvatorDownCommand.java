package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class AidanvatorDownCommand extends Command
{
    public AidanvatorDownCommand()
    {
        requires(Robot.getAidanvator());
    }

    @Override
    protected void initialize()
    {

    }

    @Override
    protected void execute()
    {
        Robot.getAidanvator().up();
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
