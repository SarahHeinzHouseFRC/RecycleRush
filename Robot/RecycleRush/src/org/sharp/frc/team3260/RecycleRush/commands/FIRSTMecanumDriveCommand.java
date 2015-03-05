package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class FIRSTMecanumDriveCommand extends Command
{
    Joystick driveJoystick = OI.getInstance().getMainGamepad();

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

        rotation = Util.handleDeadband(rotation, Constants.joystickDeadband.getDouble());
        strafe = Util.handleDeadband(strafe, Constants.joystickDeadband.getDouble());
        forward = Util.handleDeadband(forward, Constants.joystickDeadband.getDouble());

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
