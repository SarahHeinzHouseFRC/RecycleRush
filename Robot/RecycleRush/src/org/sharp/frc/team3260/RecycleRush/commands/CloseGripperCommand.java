package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class CloseGripperCommand extends Command
{
    @Override
    protected void initialize()
    {
        requires(Robot.getGripper());
    }

    @Override
    protected void execute()
    {
        Robot.getGripper().closeGripper();
    }

    @Override
    protected boolean isFinished()
    {
        return true;
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
