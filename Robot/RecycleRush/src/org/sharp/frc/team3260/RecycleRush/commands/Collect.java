package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.subsystems.Collector;
import org.sharp.frc.team3260.RecycleRush.subsystems.Pivot;

/**
 * Get the robot set to collect balls.
 */
public class Collect extends CommandGroup
{
    public Collect()
    {
        addSequential(new SetCollectionSpeed(Collector.FORWARD));
        addParallel(new CloseClaw());
        addSequential(new SetPivotSetpoint(Pivot.COLLECT));
        addSequential(new WaitForBall());
    }
}
