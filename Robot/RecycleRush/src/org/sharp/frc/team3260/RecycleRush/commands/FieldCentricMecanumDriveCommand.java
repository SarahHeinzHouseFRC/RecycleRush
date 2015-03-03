package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class FieldCentricMecanumDriveCommand extends Command
{
    Joystick driveJoystick = OI.getInstance().getMainGamepad();

    public FieldCentricMecanumDriveCommand()
    {
        requires(DriveTrain.getInstance());
    }

    protected void initialize()
    {
        DriveTrain.getInstance().changeControlMode(CANTalon.ControlMode.PercentVbus);
    }

    protected void execute()
    {
        double strafe = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_X);
        double forward = -driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_Y);
        double rotation = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_X);

        rotation = Util.handleDeadband(rotation, Constants.joystickDeadband.getDouble());
        strafe = Util.handleDeadband(strafe, Constants.joystickDeadband.getDouble());
        forward = Util.handleDeadband(forward, Constants.joystickDeadband.getDouble());

        SmartDashboard.putNumber("Drive Joystick X", strafe);
        SmartDashboard.putNumber("Drive Joystick Y", forward);
        SmartDashboard.putNumber("Drive Joystick Rotation", rotation);

        if(DriveTrain.getInstance().getIMU() == null)
        {
            DriveTrain.getInstance().mecanumDrive_Cartesian(strafe, forward, rotation, 0, false);
        }
        else
        {
            DriveTrain.getInstance().mecanumDrive_Cartesian(strafe, forward, rotation, DriveTrain.getInstance().getIMU().getYaw(), true);

            SmartDashboard.putNumber("Gyro Yaw", DriveTrain.getInstance().getIMU().getYaw());
        }
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
        DriveTrain.getInstance().stop();
    }

    protected void interrupted()
    {
        end();
    }
}