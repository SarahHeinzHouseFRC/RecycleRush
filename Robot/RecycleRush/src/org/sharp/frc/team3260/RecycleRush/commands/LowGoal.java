package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.subsystems.Collector;
import org.sharp.frc.team3260.RecycleRush.subsystems.Pivot;

/**
 * Spit the ball out into the low goal assuming that the robot is in front of it.
 */
public class LowGoal extends CommandGroup
{
    public LowGoal()
    {
        addSequential(new SetPivotSetpoint(Pivot.LOW_GOAL));
        addSequential(new SetCollectionSpeed(Collector.REVERSE));
        addSequential(new ExtendShooter());
    }
}
