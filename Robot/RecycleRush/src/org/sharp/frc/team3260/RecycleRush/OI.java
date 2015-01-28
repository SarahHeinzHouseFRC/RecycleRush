package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.joystick.XBoxGamepad;


public class OI
{
    private static OI instance;

    public XBoxGamepad mainGamepad;
    public XBoxGamepad manipulatorGamepad;

    public Button manipulatorGamepadA = new JoystickButton(manipulatorGamepad, XBoxGamepad.BTN_A),
            manipulatorGamepadB = new JoystickButton(manipulatorGamepad, XBoxGamepad.BTN_B),
            manipulatorGamepadX = new JoystickButton(manipulatorGamepad, XBoxGamepad.BTN_X),
            manipulatorGamepadY = new JoystickButton(manipulatorGamepad, XBoxGamepad.BTN_Y);

    public OI()
    {
        mainGamepad = new XBoxGamepad(Constants.mainGamepadID.getInt());
        manipulatorGamepad = new XBoxGamepad(Constants.manipulatorGamepadID.getInt());

        SmartDashboard.putData("SHARPDrive", new SHARPDriveCommand());
        SmartDashboard.putData("Field Centric Mecanum Drive", new FieldCentricMecanumDriveCommand());

        manipulatorGamepadA.whileHeld(new AidanvatorUpCommand());
        manipulatorGamepadB.whileHeld(new AidanvatorDownCommand());

        manipulatorGamepadX.whenReleased(new CloseGripperCommand());
        manipulatorGamepadY.whenReleased(new OpenGripperCommand());
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
