package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class SHARPMecanumDriveCommand extends Command
{
    private static final double ROTATION_DEADBAND = 0.05;

    Joystick driveJoystick = OI.getInstance().getMainGamepad();
    Joystick secondJoystick = OI.getInstance().getManipulatorGamepad();

    public SHARPMecanumDriveCommand()
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

        double secondJoystickForward = secondJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_Y);
        double secondJoystickStrafe = secondJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_X);

        rotation = Math.abs(rotation) > ROTATION_DEADBAND ? rotation : 0;
        strafe = Math.abs(strafe) > ROTATION_DEADBAND ? strafe : 0;
        forward = Math.abs(forward) > ROTATION_DEADBAND ? forward : 0;

        if(Math.abs(forward) < 0.1 && Math.abs(secondJoystickForward) > ROTATION_DEADBAND)
        {
            strafe = secondJoystickForward * 0.5;
        }
        
        if(Math.abs(strafe) < 0.1 && Math.abs(secondJoystickStrafe) > ROTATION_DEADBAND)
        {
            strafe = secondJoystickStrafe * 0.5; 
        }
        
        if(DriveTrain.getInstance().getIMU() == null)
        {
            DriveTrain.getInstance().stockMecanumDrive(strafe, forward, rotation, 0);
        }
        else
        {
            DriveTrain.getInstance().stockMecanumDrive(strafe, forward, rotation, DriveTrain.getInstance().getIMU().getYaw(), true);

            SmartDashboard.putNumber("Gyro Yaw", DriveTrain.getInstance().getIMU().getYaw());
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
