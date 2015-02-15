package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;

public class SwitchGamepadsCommand extends Command
{
    public SwitchGamepadsCommand()
    {
        requires(DriveTrain.getInstance());
        requires(Elevator.getInstance());
        requires(Gripper.getInstance());
    }

    @Override
    protected void initialize()
    {
        DriveTrain.getInstance().stop();

        Elevator.getInstance().stop();

        OI.getInstance().switchGamepads();
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
