package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.commands.FieldCentricMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPDriveCommand;
import org.sharp.frc.team3260.RecycleRush.joystick.XBoxGamepad;

/**
 * The operator interface of the robot, it has been simplified from the real
 * robot to allow control with a single PS3 joystick. As a result, not all
 * functionality from the real robot is available.
 */
public class OI
{
    private static OI instance;

    public XBoxGamepad mainGamepad;
    public XBoxGamepad manipulatorGamepad;

    public OI()
    {
        mainGamepad = new XBoxGamepad(Constants.mainGamepadID.getInt());
        manipulatorGamepad = new XBoxGamepad(Constants.manipulatorGamepadID.getInt());

        SmartDashboard.putData("SHARPDrive", new SHARPDriveCommand());
        SmartDashboard.putData("Field Centric Mecanum Drive", new FieldCentricMecanumDriveCommand());
    }

    public static OI getInstance()
    {
        if (instance == null)
        {
            instance = new OI();
        }

        return instance;
    }

    public XBoxGamepad getMainGamepad()
    {
        return mainGamepad;
    }

    public XBoxGamepad getManipulatorGamepad()
    {
        return manipulatorGamepad;
    }
}
