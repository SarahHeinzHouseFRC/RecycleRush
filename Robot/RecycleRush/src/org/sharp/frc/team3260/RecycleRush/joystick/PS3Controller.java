package org.sharp.frc.team3260.RecycleRush.joystick;

import edu.wpi.first.wpilibj.Joystick;

/**
 * @author Eddie
 */
public class PS3Controller
{
	private final Joystick gamepad;

	private static final int JS_LEFT_X = 1,
			JS_LEFT_Y = 2,
			JS_RIGHT_X = 4,
			JS_RIGHT_Y = 5,
			JS_TRIGGERS = 3;

	private static final int DPAD_X = 6,
			DPAD_Y = 8;

	private static final int BTN_X = 1,
			BTN_CIRCLE = 2,
			BTN_SQUARE = 3,
			BTN_TRIANGLE = 4,
			BTN_LEFT_BUMPER = 5,
			BTN_RIGHT_BUMPER = 6,
			BTN_START = 8,
			BTN_SELECT = 7,
			BTN_PS = 9,
			BTN_JS_LEFT = 9,
			BTN_JS_RIGHT = 10,
			BTN_DPAD_UP = 11,
			BTN_DPAD_DOWN = 12;

	public static final double TRIGGER_THRESHOLD = 0.5;

	public PS3Controller(int id)
	{
		gamepad = new Joystick(id);
	}

	public double getJoystickLeftX()
	{
		return gamepad.getRawAxis(JS_LEFT_X);
	}

	public double getJoystickLeftY()
	{
		return -gamepad.getRawAxis(JS_LEFT_Y);
	}

	public double getJoystickRightX()
	{
		return gamepad.getRawAxis(JS_RIGHT_X);
	}

	public double getJoystickRightY()
	{
		return -gamepad.getRawAxis(JS_RIGHT_Y);
	}

	public double getJoystickLeftMagnitude()
	{
		return Math.sqrt(Math.pow(getJoystickLeftX(), 2) + Math.pow(-getJoystickLeftY(), 2));
	}

	public double getJoystickRightMagnitude()
	{
		return Math.sqrt(Math.pow(getJoystickRightX(), 2) + Math.pow(getJoystickRightY(), 2));
	}

	public double getJoystickLeftDirectionRadians()
	{
		return Math.atan2(getJoystickLeftX(), getJoystickLeftY());
	}

	public double getJoystickLeftDirectionDegrees()
	{
		return Math.toDegrees(getJoystickLeftDirectionRadians());
	}

	public double getJoystickRightDirectionRadians()
	{
		return Math.atan2(getJoystickRightX(), -getJoystickRightY());
	}

	public double getJoystickRightDirectionDegrees()
	{
		return Math.toDegrees(getJoystickRightDirectionRadians());
	}

	public double getDPadX()
	{
		return gamepad.getRawAxis(DPAD_X);
	}

	public double getDPadY()
	{
		if(gamepad.getRawButton(BTN_DPAD_UP))
		{
			return 1.0;
		} else if(gamepad.getRawButton(BTN_DPAD_DOWN))
		{
			return -1.0;
		}

		return 0.0;
	}

	public boolean getButtonLeftBumper()
	{
		return gamepad.getRawButton(BTN_LEFT_BUMPER);
	}

	public boolean getButtonRightBumper()
	{
		return gamepad.getRawButton(BTN_RIGHT_BUMPER);
	}

	public boolean getButtonLeftJoystick()
	{
		return gamepad.getRawButton(BTN_JS_LEFT);
	}

	public boolean getButtonRightJoystick()
	{
		return gamepad.getRawButton(BTN_JS_RIGHT);
	}

	public boolean getButtonX()
	{
		return gamepad.getRawButton(BTN_X);
	}

	public boolean getButtonCircle()
	{
		return gamepad.getRawButton(BTN_CIRCLE);
	}

	public boolean getButtonSquare()
	{
		return gamepad.getRawButton(BTN_SQUARE);
	}

	public boolean getButtonTriangle()
	{
		return gamepad.getRawButton(BTN_TRIANGLE);
	}

	public boolean getButtonStart()
	{
		return gamepad.getRawButton(BTN_START);
	}

	public boolean getButtonSelect()
	{
		return gamepad.getRawButton(BTN_SELECT);
	}

	public boolean getButtonPS()
	{
		return gamepad.getRawButton(BTN_PS);
	}

	public double getTriggerValue()
	{
		return -gamepad.getRawAxis(JS_TRIGGERS);
	}

	public boolean getRightTrigger()
	{
		if(getTriggerValue() >= TRIGGER_THRESHOLD)
		{
			return true;
		}

		return false;
	}

	public boolean getLeftTrigger()
	{
		if(getTriggerValue() <= -TRIGGER_THRESHOLD)
		{
			return true;
		}

		return false;
	}
}
