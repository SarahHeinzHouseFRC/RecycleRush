package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ElevatorDownCommand extends Command
{
    private double speed = 1.0;

    public ElevatorDownCommand()
    {
        requires(Elevator.getInstance());
    }

    public ElevatorDownCommand(double speed)
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
        Elevator.getInstance().down(speed);
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
