package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.ChainLifter;

public class ChainlifterDownCommand extends Command
{
    public ChainlifterDownCommand()
    {
        requires(ChainLifter.getInstance());
    }

    @Override
    protected void initialize()
    {

    }

    @Override
    protected void execute()
    {
        ChainLifter.getInstance().down();
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
