package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;


public class CanAndLiftAutonomousCommand extends CommandGroup
{

    public CanAndLiftAutonomousCommand()
    {

        addSequential(new ZeroGyroCommand());
        addSequential(new ZeroElevatorEncoderCommand());
        addSequential(new CloseElevatorArmsCommand());
        addSequential(new RobotIdleCommand(250));
        addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.RECYCLING_CAN));
        addSequential(new RobotIdleCommand(250));
        addSequential(new DriveAtSpeedCommand(-.45, 2.5));


    }

}
