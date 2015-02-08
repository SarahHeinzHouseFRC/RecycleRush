package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ElevatorUpCommand extends Command
{
    private double speed = 1.0;

    public ElevatorUpCommand()
    {
        requires(Elevator.getInstance());
    }

    public ElevatorUpCommand(double speed)
    {
        requires(Elevator.getInstance());

        this.speed = speed;
    }

    @Override
    protected void initialize()
    {
    }

    @Override
    protected void execute()
    {
        Elevator.getInstance().up(speed);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
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
