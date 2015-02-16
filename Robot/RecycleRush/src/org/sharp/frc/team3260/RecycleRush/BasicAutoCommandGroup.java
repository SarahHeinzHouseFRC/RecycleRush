package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.commands.RobotIdleCommand;

public class BasicAutoCommandGroup extends CommandGroup
{
    public BasicAutoCommandGroup()
    {
        addSequential(new RobotIdleCommand(15));
    }
}
