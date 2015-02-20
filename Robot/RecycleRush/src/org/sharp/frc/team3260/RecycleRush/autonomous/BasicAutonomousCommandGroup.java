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
        addSequential(new DriveDistanceCommand(1100, 5));
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(6)));
        addSequential(new OpenGripperCommand());
        addSequential(new DriveDistanceCommand(-400, 1));
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(0)));
        addSequential(new DriveDistanceCommand(450, 1));
        addSequential(new CloseGripperCommand());
        addSequential(new RobotIdleCommand(250));
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(8)));
        addSequential(new ZeroGyroCommand());
        addSequential(new RobotIdleCommand(250));
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(2)));
        //        addSequential(new DriveDistanceCommand(4550, 5));
        addSequential(new ZeroGyroCommand());
        addSequential(new DriveAtSpeedCommand(-0.45, 2.5));
        addSequential(new RobotIdleCommand(500));
        addSequential(new ZeroGyroCommand());
        addSequential(new RobotIdleCommand(250));
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new RobotIdleCommand(250));
        addSequential(new OpenGripperCommand());
        addSequential(new ZeroGyroCommand());
    }
}
