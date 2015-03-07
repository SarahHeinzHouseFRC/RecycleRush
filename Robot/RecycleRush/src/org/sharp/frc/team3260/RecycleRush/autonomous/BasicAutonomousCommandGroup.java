package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class BasicAutonomousCommandGroup extends CommandGroup
{
    public BasicAutonomousCommandGroup()
    {
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(3)));
        addSequential(new CloseGripperCommand());
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(7)));
        addSequential(new ZeroGyroCommand());
        addSequential(new RobotIdleCommand(250));
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(2)));
        addSequential(new ZeroGyroCommand());
        addSequential(new DriveAtSpeedCommand(0.45, 2.5));
        addSequential(new RobotIdleCommand(500));
    }
}
