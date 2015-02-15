package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;

public class IdleCommand extends Command
{
    double idleTime;

    public IdleCommand(double timeToIdle)
    {
        requires(DriveTrain.getInstance());
        requires(Elevator.getInstance());
        requires(Gripper.getInstance());

        idleTime = timeToIdle / 1000;

        setTimeout(idleTime);
    }

    @Override
    protected void initialize()
    {
        Robot.getInstance().getLogger().info("Idling for " + idleTime);
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
