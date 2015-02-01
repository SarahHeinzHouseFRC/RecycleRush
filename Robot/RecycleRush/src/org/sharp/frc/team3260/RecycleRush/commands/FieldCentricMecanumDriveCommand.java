package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;

public class FieldCentricMecanumDriveCommand extends Command
{
    public double ROTATION_DEADBAND = 0.2;

    /**
     * The maximum speed that the robot is allowed to rotate at. The joystick
     * value is scaled down to this value.
     */
    public static final double MAX_ROTATION = 0.5;

    public FieldCentricMecanumDriveCommand()
    {
        requires(DriveTrain.getInstance());
    }

    /**
     * Does nothing.
     */
    protected void initialize()
    {
    }

    /**
     * Updates the robot's speed based on the joystick values.
     */
    protected void execute()
    {
        // This is the joystick that we use as input for driving.
        Joystick driveJoystick = OI.getInstance().getMainGamepad();

        // Store the axis values.
        double x = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_X);
        double y = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_LEFT_Y);
        double rotation = driveJoystick.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_X);

        // This will hold the scaled rotation value. We scale down this value
        // because otherwise the robot is too hard ot control with the joystick 
        // twist and we don't need our full possible rotation speed (its pretty
        // fast).
        double scaledRotation = rotation;

        // We implemented a deadband in order to filter out small accidental 
        // twists of the stick. If the rotation value is less than the deadband, we don't rotate.
        if (Math.abs(rotation) < ROTATION_DEADBAND)
        {
            scaledRotation = 0;
        }
        else
        {
            // Scale rotation down so it is never greater than MAX_ROTATION.
            scaledRotation += rotation < 0 ? ROTATION_DEADBAND : -ROTATION_DEADBAND;
            scaledRotation = (scaledRotation / (1.0 - ROTATION_DEADBAND)) * MAX_ROTATION;
        }

        // Send debugging values.
        SmartDashboard.putNumber("Joystick X", x);
        SmartDashboard.putNumber("Joystick Y", y);
        SmartDashboard.putNumber("Rotation", rotation);
        SmartDashboard.putNumber("Scaled Rotation", scaledRotation);

        DriveTrain.getInstance().mecanumDrive_Cartesian(x, -y, scaledRotation, DriveTrain.getInstance().getIMU().getYaw());
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