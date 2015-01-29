package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;

/**
 * This command drives the robot over a given distance with simple proportional
 * control This command will drive a given distance limiting to a maximum speed.
 */
public class DriveForwardCommand extends Command
{
    private double driveForwardSpeed;
    private double distance;
    private double error;
    private final double TOLERANCE = .1;
    private final double kP = -1.0 / 5.0;

    public DriveForwardCommand()
    {
        this(10, 0.5);
    }

    public DriveForwardCommand(double dist)
    {
        this(dist, 0.5);
    }

    public DriveForwardCommand(double dist, double maxSpeed)
    {
        requires(Robot.getDrivetrain());
        distance = dist;
        driveForwardSpeed = maxSpeed;
    }

    protected void initialize()
    {
        Robot.getDrivetrain().resetEncoders();

        setTimeout(2);
    }

    protected void execute()
    {
        error = (distance - Robot.getDrivetrain().getAverageEncoderDistance());

        if (driveForwardSpeed * kP * error >= driveForwardSpeed)
        {
            Robot.getDrivetrain().tankDrive(driveForwardSpeed, driveForwardSpeed);
        }
        else
        {
            Robot.getDrivetrain().tankDrive(driveForwardSpeed * kP * error, driveForwardSpeed * kP * error);
        }
    }

    protected boolean isFinished()
    {
        return (Math.abs(error) <= TOLERANCE) || isTimedOut();
    }

    protected void end()
    {
        Robot.getDrivetrain().stop();
    }

    protected void interrupted()
    {
        end();
    }
}
