package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.buttons.Button;
import org.sharp.frc.team3260.RecycleRush.commands.CloseGripperCommand;
import org.sharp.frc.team3260.RecycleRush.commands.OpenGripperCommand;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.joystick.triggers.AxisButton;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class OI
{
    private static OI instance;

    public SHARPGamepad mainGamepad, manipulatorGamepad;

    public Button manipulatorGamepadA, manipulatorGamepadB, manipulatorGamepadLeftTrigger, manipulatorGamepadRightTrigger;

    public OI()
    {
        mainGamepad = new SHARPGamepad(Constants.mainGamepadID.getInt());
        manipulatorGamepad = new SHARPGamepad(Constants.manipulatorGamepadID.getInt());

        //        manipulatorGamepadA = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_A);
        //        manipulatorGamepadB = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_B);
        manipulatorGamepadLeftTrigger = new AxisButton(manipulatorGamepad, SHARPGamepad.TRIGGER_LEFT_AXIS, 0.5);
        manipulatorGamepadRightTrigger = new AxisButton(manipulatorGamepad, SHARPGamepad.TRIGGER_RIGHT_AXS, 0.5);

        //        manipulatorGamepadA.whileHeld(new ElevatorUpCommand(0.3));
        //        manipulatorGamepadB.whileHeld(new ElevatorDownCommand(0.3));

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

    public SHARPGamepad getMainGamepad()
    {
        return mainGamepad;
    }

    public SHARPGamepad getManipulatorGamepad()
    {
        return manipulatorGamepad;
    }

    public void checkControls()
    {
        Elevator.getInstance().up(-manipulatorGamepad.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_Y));
    }
}
