package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

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
            requires(Robot.getDrivetrain());
        }
    }

    @Override
    protected void initialize()
    {
        Robot.getDrivetrain().setRotatingToTarget(true);

        Robot.getDrivetrain().setRotationTarget(yawTarget);
    }

    @Override
    protected void execute()
    {
        if (blocking)
        {
            double error = Robot.getDrivetrain().getIMU().getYaw() - yawTarget;

            Robot.getDrivetrain().mecanumDrive_Cartesian(0, 0, yawTarget > 0 ? 0.5 : -0.5, Robot.getDrivetrain().getIMU().getYaw());
        }
    }

    @Override
    protected boolean isFinished()
    {
        return !blocking || Robot.getDrivetrain().getIMU().getYaw() == yawTarget;
    }

    @Override
    protected void end()
    {
        Robot.getDrivetrain().setRotatingToTarget(false);
    }

    @Override
    protected void interrupted()
    {
        end();
    }
}
