package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class RotateToHeadingCommand extends Command
{
    private boolean isBlocking;

    private double rotationTarget;

    public RotateToHeadingCommand(double rotationTarget, boolean isBlocking)
    {
        if(isBlocking)
        {
            requires(DriveTrain.getInstance());
        }

        this.rotationTarget = rotationTarget;

        this.isBlocking = isBlocking;

        setTimeout(5);
    }

    public RotateToHeadingCommand(double rotationTarget, double timeout, boolean isBlocking)
    {
        if(isBlocking)
        {
            requires(DriveTrain.getInstance());
        }

        this.rotationTarget = rotationTarget;

        this.isBlocking = isBlocking;

        setTimeout(timeout);
    }

    @Override
    protected void initialize()
    {
        DriveTrain.getInstance().setRotationTarget(rotationTarget);
    }

    @Override
    protected void execute()
    {
        if(isBlocking)
        {
            DriveTrain.getInstance().mecanumDrive_Cartesian(0, 0, 0, DriveTrain.getInstance().getIMU().getYaw(), true);
        }
    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut() || DriveTrain.getInstance().reachedRotationTarget();
    }

    @Override
    protected void end()
    {
        DriveTrain.getInstance().setRotationTarget(DriveTrain.getInstance().getIMU().getYaw());
    }

    @Override
    protected void interrupted()
    {
    }
}
