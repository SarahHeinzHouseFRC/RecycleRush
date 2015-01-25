package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class ZeroGyro extends Command
{
    @Override
    protected void initialize()
    {
        requires(Robot.getDrivetrain());
    }

    @Override
    protected void execute()
    {
        Robot.getDrivetrain().zeroGyro();
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
