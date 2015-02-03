package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;

public class OpenGripperCommand extends Command
{
    public OpenGripperCommand()
    {
        requires(Gripper.getInstance());
    }

    @Override
    protected void initialize()
    {
    }

    @Override
    protected void execute()
    {
        Gripper.getInstance().openGripper();
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
