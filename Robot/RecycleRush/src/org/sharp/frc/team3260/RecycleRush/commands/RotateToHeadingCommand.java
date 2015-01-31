package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class RotateToHeadingCommand extends Command
{
    boolean blocking;

    double yawTarget;

    public RotateToHeadingCommand(double yawTarget, boolean blocking)
    {
        this.blocking = blocking;

        this.yawTarget = yawTarget;

        if (blocking)
        {
            requires(DriveTrain.getInstance());
        }
    }

    @Override
    protected void initialize()
    {
        DriveTrain.getInstance().setRotatingToTarget(true);

        DriveTrain.getInstance().setRotationTarget(yawTarget);
    }

    @Override
    protected void execute()
    {
        if (blocking)
        {
            double error = DriveTrain.getInstance().getIMU().getYaw() - yawTarget;

            DriveTrain.getInstance().mecanumDrive_Cartesian(0, 0, error > 0 ? 0.5 : -0.5, DriveTrain.getInstance().getIMU().getYaw());
        }
    }

    @Override
    protected boolean isFinished()
    {
        return !blocking || DriveTrain.getInstance().getIMU().getYaw() == yawTarget;
    }

    @Override
    protected void end()
    {
        DriveTrain.getInstance().setRotatingToTarget(false);
    }

    @Override
    protected void interrupted()
    {
        end();
    }
}
