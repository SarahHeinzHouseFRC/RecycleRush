package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.commands.DriveForward;

/**
 * The operator interface of the robot, it has been simplified from the real
 * robot to allow control with a single PS3 joystick. As a result, not all
 * functionality from the real robot is available.
 */
public class OI
{
	public Joystick joystick;

	public OI()
	{
		joystick = new Joystick(0);

		// SmartDashboard Buttons
		SmartDashboard.putData("Drive Forward", new DriveForward(2.25));
		SmartDashboard.putData("Drive Backward", new DriveForward(-2.25));
	}

	public Joystick getJoystick()
	{
		return joystick;
	}
}
