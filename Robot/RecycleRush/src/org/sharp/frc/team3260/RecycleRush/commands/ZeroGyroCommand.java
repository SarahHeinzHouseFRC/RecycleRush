package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class ZeroGyroCommand extends Command
{
    @Override
    protected void initialize()
    {
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void execute()
    {
        DriveTrain.getInstance().zeroGyro();
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
