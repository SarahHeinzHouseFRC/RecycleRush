package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.joystick.triggers.AxisButton;
import org.sharp.frc.team3260.RecycleRush.joystick.triggers.HatButton;
import org.sharp.frc.team3260.RecycleRush.joystick.triggers.TalonLimitSwitchButton;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class OI
{
    private static OI instance;

    public SHARPGamepad mainGamepad, manipulatorGamepad;

    public Button elevatorTalonReverseLimitSwitchButton;

    public Button mainGamepadSelectButton, manipulatorGamepadSelectButton;

    public Button manipulatorGamepadA, manipulatorGamepadB, manipulatorGamepadX, manipulatorGamepadY;

    public Button mainGamepadLeftBumper, mainGamepadRightBumper;

    public Button manipulatorGamepadLeftBumper, manipulatorGamepadRightBumper;
    public Button manipulatorGamepadLeftTrigger, manipulatorGamepadRightTrigger;

    public Button manipulatorGamepadHatDown;

    public OI()
    {
        elevatorTalonReverseLimitSwitchButton = new TalonLimitSwitchButton(Elevator.getInstance().getTalon(), false);

        mainGamepad = new SHARPGamepad(Constants.mainGamepadID.getInt());
        manipulatorGamepad = new SHARPGamepad(Constants.manipulatorGamepadID.getInt());

        mainGamepadSelectButton = new JoystickButton(mainGamepad, SHARPGamepad.BUTTON_SELECT);
        manipulatorGamepadSelectButton = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_SELECT);

        manipulatorGamepadLeftTrigger = new AxisButton(manipulatorGamepad, SHARPGamepad.TRIGGER_LEFT_AXIS, 0.5);
        manipulatorGamepadRightTrigger = new AxisButton(manipulatorGamepad, SHARPGamepad.TRIGGER_RIGHT_AXS, 0.5);

        mainGamepadLeftBumper = new JoystickButton(mainGamepad, SHARPGamepad.BUTTON_LEFT_BUMPER);
        mainGamepadRightBumper = new JoystickButton(mainGamepad, SHARPGamepad.BUTTON_RIGHT_BUMPER);

        manipulatorGamepadLeftBumper = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_LEFT_BUMPER);
        manipulatorGamepadRightBumper = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_RIGHT_BUMPER);

        manipulatorGamepadA = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_A);
        manipulatorGamepadB = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_B);
        manipulatorGamepadX = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_X);
        manipulatorGamepadY = new JoystickButton(manipulatorGamepad, SHARPGamepad.BUTTON_Y);

        manipulatorGamepadHatDown = new HatButton(manipulatorGamepad, 180);

        elevatorTalonReverseLimitSwitchButton.whenPressed(new ZeroElevatorEncoderCommand());

        mainGamepadSelectButton.whenReleased(new SwitchGamepadsCommand());
        manipulatorGamepadSelectButton.whenReleased(new SwitchGamepadsCommand());

        manipulatorGamepadLeftTrigger.whenReleased(new CloseElevatorArmsCommand());
        manipulatorGamepadRightTrigger.whenReleased(new OpenElevatorArmsCommand());

        mainGamepadLeftBumper.whenReleased(new RotateToHeadingCommand(135, false));
        mainGamepadRightBumper.whenReleased(new RotateToHeadingCommand(-135, false));

        manipulatorGamepadLeftBumper.whenReleased(new CloseLowerArmsCommand());
        manipulatorGamepadRightBumper.whenReleased(new OpenLowerArmsCommand());

        manipulatorGamepadA.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.GROUND));
        manipulatorGamepadB.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.TWO_TOTE));
        manipulatorGamepadX.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.TWO_TOTE));
        manipulatorGamepadY.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.LOADING_HEIGHT));

        manipulatorGamepadHatDown.whenReleased(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.TOP));

        instance = this;
    }

    public static OI getInstance()
    {
        if(instance == null)
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
        if((Math.abs(manipulatorGamepad.getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_Y)) > 0.5))
        {
            Elevator.getInstance().changeElevatorMode(false);
        }

        if(OI.getInstance().mainGamepad.getRawButton(SHARPGamepad.BUTTON_Y))
        {
            DriveTrain.getInstance().zeroGyro();

            DriveTrain.getInstance().zeroEncoders();
        }
    }

    public void switchGamepads()
    {
        SHARPGamepad temp = mainGamepad;

        mainGamepad = manipulatorGamepad;

        manipulatorGamepad = temp;
    }
}
