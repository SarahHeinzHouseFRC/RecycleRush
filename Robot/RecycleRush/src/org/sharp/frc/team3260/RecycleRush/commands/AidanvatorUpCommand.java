package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class AidanvatorUpCommand extends Command
{
    public AidanvatorUpCommand()
    {
        requires(Robot.getElevator());
    }

    @Override
    protected void initialize()
    {

    }

    @Override
    protected void execute()
    {
        Robot.getElevator().up();
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
