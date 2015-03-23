package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

/**
 * Created by SHARP on 3/21/2015.
 */
public class ThreeToteAutonomous extends CommandGroup
{

/*

close totel
lift 2 tote hight

rorate 180

grip bootom

open main
arm to ground
down to bottom reapt
 */

    public ThreeToteAutonomous()
    {
        addSequential(new CloseElevatorArmsCommand());
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(2)));
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new ZeroGyroCommand());
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new DriveAtSpeedCommand(.4, .5));
        addSequential(new CloseLowerArmsCommand());
        addSequential(new OpenElevatorArmsCommand());
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(0)));
        addSequential(new OpenLowerArmsCommand());
        addSequential(new CloseElevatorArmsCommand());
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(2)));
        addSequential(new DriveAtSpeedCommand(.4, 1));
        addSequential(new CloseLowerArmsCommand());
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new DriveAtSpeedCommand(.3, 4000));
        addSequential(new OpenElevatorArmsCommand());
        addSequential(new OpenLowerArmsCommand());
        addSequential(new DriveAtSpeedCommand(-.3, .5));

    }
}
