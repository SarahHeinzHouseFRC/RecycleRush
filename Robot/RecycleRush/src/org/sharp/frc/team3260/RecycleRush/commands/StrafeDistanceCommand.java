package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class StrafeDistanceCommand extends Command
{
    int ticks;

    boolean strafeRight;

    double timeout;

    public StrafeDistanceCommand(int ticks, boolean strafeRight, double timeout)
    {
        this.ticks = ticks;

        this.strafeRight = strafeRight;

        this.timeout = timeout;
    }

    @Override
    protected void initialize()
    {
        setTimeout(timeout);

        DriveTrain.getInstance().getLogger().info("Strafing " + (strafeRight ? "Right " : "Left ") + " initiated, distance of " + ticks + " ticks, timeout is " + timeout + " seconds.");

        DriveTrain.getInstance().zeroEncoders();

        DriveTrain.getInstance().clearAccumulatedI();
        
        if(strafeRight)
        {
            DriveTrain.getInstance().setDriveEncoderTargets(ticks, -ticks, -ticks, ticks);
        }
        else
        {
            DriveTrain.getInstance().setDriveEncoderTargets(-ticks, ticks, ticks, -ticks);
        }
    }

    @Override
    protected void execute()
    {

    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut() || DriveTrain.getInstance().atDriveTarget();
    }

    @Override
    protected void end()
    {
        DriveTrain.getInstance().changeControlMode(CANTalon.ControlMode.PercentVbus);

        DriveTrain.getInstance().stop();

        DriveTrain.getInstance().getLogger().info("Strafing " + (strafeRight ? "Right " : "Left ") + " ended.");
    }

    @Override
    protected void interrupted()
    {
        DriveTrain.getInstance().getLogger().warn("Strafing " + (strafeRight ? "Right " : "Left ") + "was interrupted.");
    }
}
