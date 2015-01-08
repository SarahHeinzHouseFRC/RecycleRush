package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

/**
 * This command allows a gamepad to drive the robot. It is always running
 * except when interrupted by another command.
 */
public class DriveWithJoystick extends Command
{
    public DriveWithJoystick()
    {
        requires(Robot.drivetrain);
    }

    protected void initialize()
    {
    }

    protected void execute()
    {
        Robot.drivetrain.tankDrive(Robot.oi.getJoystick());
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
        Robot.drivetrain.stop();
    }

    protected void interrupted()
    {
        end();
    }
}