package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

/**
 * This command drives the robot over a given distance with simple proportional
 * control This command will drive a given distance limiting to a maximum speed.
 */
public class DriveDistanceCommand extends Command
{
    private double distance;

    public DriveDistanceCommand()
    {
        this(10 * 162);
    }

    public DriveDistanceCommand(double dist)
    {
        requires(DriveTrain.getInstance());

        distance = dist;
    }

    protected void initialize()
    {
        setTimeout(5);

        DriveTrain.getInstance().getLogger().info("DriveDistanceCommand initiated, distance set to " + distance + ".");

        DriveTrain.getInstance().setDriveEncoderTargets(distance, distance, distance, distance);
    }

    protected void execute()
    {
    }

    protected boolean isFinished()
    {
        return DriveTrain.getInstance().atDriveTarget();
    }

    protected void end()
    {
        DriveTrain.getInstance().getLogger().info("DriveDistanceCommand ended.");

        DriveTrain.getInstance().stop();
    }

    protected void interrupted()
    {
        end();
    }
}
