package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.joystick.XBoxGamepad;
import org.sharp.frc.team3260.RecycleRush.joystick.triggers.AxisButton;

public class OI
{
    private static OI instance;

    public XBoxGamepad mainGamepad;
    public XBoxGamepad manipulatorGamepad;

    public Button manipulatorGamepadA = new JoystickButton(manipulatorGamepad, XBoxGamepad.BTN_A),
            manipulatorGamepadB = new JoystickButton(manipulatorGamepad, XBoxGamepad.BTN_B),
            manipulatorGamepadLeftTrigger = new AxisButton(manipulatorGamepad, XBoxGamepad.JS_TRIGGERS, 0.5),
            manipulatorGamepadRightTrigger = new AxisButton(manipulatorGamepad, XBoxGamepad.JS_TRIGGERS, -0.5);

    public OI()
    {
        mainGamepad = new XBoxGamepad(Constants.mainGamepadID.getInt());
        manipulatorGamepad = new XBoxGamepad(Constants.manipulatorGamepadID.getInt());

        SmartDashboard.putData("SHARPDrive", new SHARPDriveCommand());
        SmartDashboard.putData("Field Centric Mecanum Drive", new FieldCentricMecanumDriveCommand());

        manipulatorGamepadA.whileHeld(new AidanvatorUpCommand());
        manipulatorGamepadB.whileHeld(new AidanvatorDownCommand());

        manipulatorGamepadLeftTrigger.whenReleased(new CloseGripperCommand());
        manipulatorGamepadRightTrigger.whenReleased(new OpenGripperCommand());

        instance = this;
    }

    public static OI getInstance()
    {
        if (instance == null)
        {
            return new OI();
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
