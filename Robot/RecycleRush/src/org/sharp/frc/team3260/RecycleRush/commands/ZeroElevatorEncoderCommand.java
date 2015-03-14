package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ZeroElevatorEncoderCommand extends Command
{
    public ZeroElevatorEncoderCommand()
    {
    }

    @Override
    protected void initialize()
    {
    }

    @Override
    protected void execute()
    {
        Elevator.getInstance().setZero();
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
