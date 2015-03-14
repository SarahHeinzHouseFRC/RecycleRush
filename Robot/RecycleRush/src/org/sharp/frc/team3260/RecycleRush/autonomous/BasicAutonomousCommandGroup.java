package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class BasicAutonomousCommandGroup extends CommandGroup
{
    public BasicAutonomousCommandGroup()
    {
        addSequential(new ZeroGyroCommand());
    }
}
