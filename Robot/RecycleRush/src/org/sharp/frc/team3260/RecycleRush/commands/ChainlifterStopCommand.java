package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.ChainLifter;

public class ChainlifterStopCommand extends Command
{
    public ChainlifterStopCommand()
    {
        requires(ChainLifter.getInstance());
    }

    @Override
    protected void initialize()
    {
        ChainLifter.getInstance().stop();
    }

    @Override
    protected void execute()
    {
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
