package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class FIRSTMecanumDriveCommand extends Command
{
    Joystick driveJoystick = OI.getInstance().getMainGamepad();

    public double ROTATION_DEADBAND = 0.1;

    public FIRSTMecanumDriveCommand()
    {
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void initialize()
    {
        DriveTrain.getInstance().changeControlMode(CANTalon.ControlMode.PercentVbus);
    }

    @Override
    protected void execute()
    {
        double strafe = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_X);
        double forward = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_Y);
        double rotation = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_X);

        rotation = Math.abs(rotation) > ROTATION_DEADBAND ? rotation : 0;

        strafe = Math.abs(strafe) > ROTATION_DEADBAND ? strafe : 0;

        forward = Math.abs(forward) > ROTATION_DEADBAND ? forward : 0;

        if(DriveTrain.getInstance().getIMU() == null || OI.getInstance().getMainGamepad().getRawButton(SHARPGamepad.BUTTON_LEFT_JOYSTICK))
        {
            DriveTrain.getInstance().stockMecanumDrive(strafe, forward, rotation, 0);
        }
        else
        {
            DriveTrain.getInstance().stockMecanumDrive(strafe, forward, rotation, DriveTrain.getInstance().getIMU().getYaw());
        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        DriveTrain.getInstance().stop();
    }

    @Override
    protected void interrupted()
    {
        end();
    }
}
