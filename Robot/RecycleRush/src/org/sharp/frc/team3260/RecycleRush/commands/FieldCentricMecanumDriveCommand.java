package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class FieldCentricMecanumDriveCommand extends Command
{
    private static final double ROTATION_DEADBAND = 0.05;

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
        double forward = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_Y);
        double rotation = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_X);

        rotation = Math.abs(rotation) > ROTATION_DEADBAND ? rotation : 0;

        strafe = Math.abs(strafe) > ROTATION_DEADBAND ? strafe : 0;

        forward = Math.abs(forward) > ROTATION_DEADBAND ? forward : 0;

        SmartDashboard.putNumber("Drive Joystick X", strafe);
        SmartDashboard.putNumber("Drive Joystick Y", forward);
        SmartDashboard.putNumber("Drive Joystick Rotation", rotation);

        if(DriveTrain.getInstance().getIMU() == null)
        {
            DriveTrain.getInstance().mecanumDrive_Cartesian(strafe, forward, rotation, 0, false);
        } else
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