package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class AidanvatorHoldPositionCommand extends Command
{
    public AidanvatorHoldPositionCommand()
    {
        requires(Robot.getElevator());
    }

    @Override
    protected void initialize()
    {
        Robot.getElevator().stop();
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
