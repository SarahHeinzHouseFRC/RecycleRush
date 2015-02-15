package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;

public class IdleCommand extends Command
{

    public IdleCommand(double timeToIdle)
    {
        setTimeout(timeToIdle);
        requires(DriveTrain.getInstance());
        requires(Elevator.getInstance());
        requires(Gripper.getInstance());
    }

    @Override
    protected void initialize()
    {

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
