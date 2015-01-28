package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.Robot;

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
        requires(Robot.getDrivetrain());
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
        double x = driveJoystick.getX();
        double y = driveJoystick.getY();
        double z = driveJoystick.getZ();
        // This will hold the scaled rotation value. We scale down this value
        // because otherwise the robot is too hard ot control with the joystick 
        // twist and we don't need our full possible rotation speed (its pretty
        // fast).
        double scaledZ = z;
        // We implemented a deadband in order to filter out small accidental 
        // twists of the stick. If the rotation value (z) is less than the 
        // deadband, we don't rotate.
        if (Math.abs(z) < ROTATION_DEADBAND)
        {
            scaledZ = 0;
        }
        else
        {
            // Scale z down so it is never greater than MAX_ROTATION.
            scaledZ += z < 0 ? ROTATION_DEADBAND : -ROTATION_DEADBAND;
            scaledZ = (scaledZ / (1.0 - ROTATION_DEADBAND)) * MAX_ROTATION;
        }
        // Send debugging values.
        SmartDashboard.putNumber("Joystick X", x);
        SmartDashboard.putNumber("Joystick Y", y);
        SmartDashboard.putNumber("Joystick Rotation", z);
        SmartDashboard.putNumber("Scaled Rotation", scaledZ);

        Robot.getDrivetrain().mecanumDrive_Cartesian(x, -y, scaledZ, Robot.getDrivetrain().getIMU().getYaw()
        );
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
        Robot.getDrivetrain().stop();
    }

    protected void interrupted()
    {
        end();
    }
}