package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.ChainLifter;

/**
 * Created by Eddie on 1/30/2015.
 */
public class ChainlifterUpCommand extends Command
{
    public ChainlifterUpCommand()
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
        ChainLifter.getInstance().up();
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
