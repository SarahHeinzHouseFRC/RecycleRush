package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.subsystems.Collector;

/**
 * Shoot the ball at the current angle.
 */
public class Shoot extends CommandGroup
{
    public Shoot()
    {
        addSequential(new WaitForPressure());
        addSequential(new SetCollectionSpeed(Collector.STOP));
        addSequential(new OpenClaw());
        addSequential(new ExtendShooter());
    }
}
