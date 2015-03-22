package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class ThreeToteAutonomousCommandGroup extends CommandGroup
{
    /*
     * Steps:
     * Zero gyro
     * Close lower arms
     * Close elevator arms
     * Rotate to -90
     * Zero gyro
     * Open lower arms
     * Raise elevator to TWO_TOTE
     * Rotate to -90
     * Zero gyro
     * Drive until hit second tote
     * Close lower arms
     * Open elevator arms
     * Lower elevator to GROUND
     * Close elevator arms
     * Open lower arms
     * Raise elevator to TWO_TOTE
     * Drive until hit third tote
     * Close lower arms
     * Rotate to -90
     * Zero gyro
     * Drive forward at 0.45 for 2.5
     * Open lower arms
     * Open elevator arms
     * Drive backward 450 ticks
     * Zero gyro
     */
    public ThreeToteAutonomousCommandGroup()
    {
        addSequential(new ZeroGyroCommand());
        addSequential(new CloseLowerArmsCommand());
        addSequential(new CloseElevatorArmsCommand());
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new ZeroGyroCommand());
        addSequential(new OpenLowerArmsCommand());
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.TWO_TOTE));
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new ZeroGyroCommand());
        addSequential(new DriveDistanceCommand(200, 1.0)); // TODO: Correct value for forward to second tote
        addSequential(new CloseLowerArmsCommand());
        addSequential(new OpenElevatorArmsCommand());
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.GROUND));
        addSequential(new CloseElevatorArmsCommand());
        addSequential(new OpenLowerArmsCommand());
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.TWO_TOTE));
        addSequential(new DriveDistanceCommand(200, 1.0)); // TODO: Correct value for forward to third tote
        addSequential(new CloseLowerArmsCommand());
        addSequential(new RotateToHeadingCommand(-90, true));
        addSequential(new ZeroGyroCommand());
        addSequential(new DriveAtSpeedCommand(0.45, 2.5));
        addSequential(new OpenElevatorArmsCommand());
        addSequential(new OpenLowerArmsCommand());
        addSequential(new DriveDistanceCommand(-450, 1.0));
        addSequential(new ZeroGyroCommand());
    }
}