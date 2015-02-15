package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.sharp.frc.team3260.RecycleRush.commands.CloseGripperCommand;
import org.sharp.frc.team3260.RecycleRush.commands.ElevatorToSetpointCommand;
import org.sharp.frc.team3260.RecycleRush.commands.OpenGripperCommand;
import org.sharp.frc.team3260.RecycleRush.commands.SwitchGamepadsCommand;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.joystick.triggers.AxisButton;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.SHARPSubsystem;

public class OI
{
    private static OI instance;

    public SHARPGamepad mainGamepad, manipulatorGamepad;

    public Button mainGamepadSelectButton, manipulatorGamepadSelectButton;

    public Button manipulatorGamepadA, manipulatorGamepadB, manipulatorGamepadX, manipulatorGamepadY;

    public Button manipulatorGamepadLeftTrigger, manipulatorGamepadRightTrigger;

    public OI()
    {
        mainGamepad = new SHARPGamepad(Constants.mainGamepadID.getInt());
        manipulatorGamepad = new SHARPGamepad(Constants.manipulatorGamepadID.getInt());

        mainGamepadSelectButton = new JoystickButton(mainGamepad, SHARPGamepad.BUTTON_SELECT);
        manipulatorGamepadSelectButton = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_SELECT);

        manipulatorGamepadLeftTrigger = new AxisButton(manipulatorGamepad, SHARPGamepad.TRIGGER_LEFT_AXIS, 0.5);
        manipulatorGamepadRightTrigger = new AxisButton(manipulatorGamepad, SHARPGamepad.TRIGGER_RIGHT_AXS, 0.5);

        manipulatorGamepadA = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_A);
        manipulatorGamepadB = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_B);
        manipulatorGamepadX = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_X);
        manipulatorGamepadY = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_Y);

        mainGamepadSelectButton.whenReleased(new SwitchGamepadsCommand());
        manipulatorGamepadSelectButton.whenReleased(new SwitchGamepadsCommand());

        manipulatorGamepadLeftTrigger.whenReleased(new CloseGripperCommand());
        manipulatorGamepadRightTrigger.whenReleased(new OpenGripperCommand());

        manipulatorGamepadA.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.GROUND, 5));
        manipulatorGamepadB.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.TWO_TOTE, 5));
        manipulatorGamepadX.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.RECYCLING_CAN, 5));
        manipulatorGamepadY.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.THREE_TOTES, 5));

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
        if ((Math.abs(manipulatorGamepad.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_Y)) > 0.5))
        {
            Elevator.getInstance().changeElevatorMode(false);
        }
    }

    public void switchGamepads()
    {
        SHARPGamepad temp = mainGamepad;

        mainGamepad = manipulatorGamepad;

        manipulatorGamepad = temp;
    }
}
