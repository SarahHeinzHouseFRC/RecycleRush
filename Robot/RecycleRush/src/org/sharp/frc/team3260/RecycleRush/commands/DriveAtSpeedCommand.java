package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class DriveAtSpeedCommand extends Command
{
    private double speed = 0.3;

    private double time = 0;

    public DriveAtSpeedCommand(double speed, double time)
    {
        requires(DriveTrain.getInstance());

        this.speed = speed;

        this.time = time;
    }

    @Override
    protected void initialize()
    {
        DriveTrain.getInstance().changeControlMode(CANTalon.ControlMode.PercentVbus);

        setTimeout(time);
    }

    @Override
    protected void execute()
    {
        if(DriveTrain.getInstance().getIMU() == null)
        {
            DriveTrain.getInstance().stockMecanumDrive(0, -speed, 0, 0);
        }
        else
        {
            DriveTrain.getInstance().stockMecanumDrive(0, -speed, 0, DriveTrain.getInstance().getIMU().getYaw());
        }
    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut();
    }

    @Override
    protected void end()
    {
        DriveTrain.getInstance().stop();
    }

    @Override
    protected void interrupted()
    {

    }
}
