package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Arms;

public class OpenElevatorArmsCommand extends Command
{
    public OpenElevatorArmsCommand()
    {
        requires(Arms.getInstance());
    }

    @Override
    protected void initialize()
    {
        Arms.getInstance().openElevatorArms();
    }

    @Override
    protected void execute()
    {
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
