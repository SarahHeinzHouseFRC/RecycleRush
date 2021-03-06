package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class SHARPMecanumDriveCommand extends Command
{
    private static final double ROTATION_DEADBAND = 0.05;

    Joystick driveJoystick = OI.getInstance().getMainGamepad();

    boolean usingSecondJoystick;

    public SHARPMecanumDriveCommand()
    {
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void initialize()
    {
        DriveTrain.getInstance().changeControlProfile(-1);
    }

    @Override
    protected void execute()
    {
        usingSecondJoystick = false;

        double strafe = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_X);
        double forward = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_Y);
        double rotation = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_X);

        rotation = Math.abs(rotation) > ROTATION_DEADBAND ? rotation : 0;
        strafe = Math.abs(strafe) > ROTATION_DEADBAND ? strafe : 0;
        forward = Math.abs(forward) > ROTATION_DEADBAND ? forward : 0;

        if(OI.getInstance().mainGamepad.getRawAxis(SHARPGamepad.TRIGGER_LEFT_AXIS) > 0.5)
        {
            double x, y;

            switch(OI.getInstance().mainGamepad.getPOV())
            {
                case 0:
                    x = 0.0;
                    y = -1.0;
                    break;

                case 90:
                    x = 1.0;
                    y = 0.0;
                    break;

                case 180:
                    x = 0.0;
                    y = 1.0;
                    break;

                case 270:
                    x = -1.0;
                    y = 0.0;
                    break;

                case -1:
                default:
                    x = 0.0;
                    y = 0.0;
                    break;
            }

            strafe = x * 0.5;

            forward = y * 0.3;

            DriveTrain.getInstance().mecanumDrive_Cartesian(strafe, forward, rotation, 0, false);
        }
        else if(DriveTrain.getInstance().getIMU() == null)
        {
            DriveTrain.getInstance().mecanumDrive_Cartesian(strafe, forward, rotation, 0, false);
        }
        else
        {
            DriveTrain.getInstance().mecanumDrive_Cartesian(strafe, forward, rotation, DriveTrain.getInstance().getIMU().getYaw(), true);
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
