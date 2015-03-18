package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class DriveDistanceCommand extends Command
{
    private double timeout = 5;

    private double distance;

    public DriveDistanceCommand(double distance)
    {
        requires(DriveTrain.getInstance());

        this.distance = distance;
    }

    public DriveDistanceCommand(double distance, double timeout)
    {
        requires(DriveTrain.getInstance());

        this.distance = distance;

        this.timeout = timeout;
    }

    protected void initialize()
    {
        setTimeout(timeout);

        DriveTrain.getInstance().getLogger().info("DriveDistanceCommand initiated, distance set to " + distance + ", timeout is " + timeout + " seconds.");

        DriveTrain.getInstance().zeroEncoders();

        DriveTrain.getInstance().clearAccumulatedI();

        DriveTrain.getInstance().setDriveEncoderTargets(distance, distance, distance, distance);
    }

    protected void execute()
    {
    }

    protected boolean isFinished()
    {
        return isTimedOut() || DriveTrain.getInstance().atDriveTarget();
    }

    protected void end()
    {
        DriveTrain.getInstance().changeControlMode(CANTalon.ControlMode.PercentVbus);

        DriveTrain.getInstance().stop();

        DriveTrain.getInstance().getLogger().info("DriveDistanceCommand ended.");
    }

    protected void interrupted()
    {
        end();
    }
}
