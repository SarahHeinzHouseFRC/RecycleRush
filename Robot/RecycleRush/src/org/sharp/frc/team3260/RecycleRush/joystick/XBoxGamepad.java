package org.sharp.frc.team3260.RecycleRush.joystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Eddie
 */
public class XBoxGamepad
{
	private final Joystick gamepad;

	public static final int JS_LEFT_X = 1,
			JS_LEFT_Y = 2,
			JS_RIGHT_X = 4,
			JS_RIGHT_Y = 5,
			JS_TRIGGERS = 3;

	private static final int DPAD_X = 6,
			DPAD_Y = 8;

	private static final int BTN_A = 1,
			BTN_B = 2,
			BTN_X = 3,
			BTN_Y = 4,
			BTN_LEFT_BUMPER = 5,
			BTN_RIGHT_BUMPER = 6,
			BTN_START = 8,
			BTN_SELECT = 7,
			BTN_JS_LEFT = 9,
			BTN_JS_RIGHT = 10;

	public static final int FILTER_JS_NONE = 0,
			FILTER_JS_SQUARED = 1,
			FILTER_JS_CUBIC = 2;

	public static final double TRIGGER_THRESHOLD = 0.8;

	public XBoxGamepad(int id)
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

	public double getFilteredAxis(int filter, double axisValue)
	{
		switch(filter)
		{
			case FILTER_JS_SQUARED:
			{
				if(axisValue > 0)
				{
					return (axisValue * axisValue);
				} else
				{
					return -(axisValue * axisValue);
				}
			}

			case FILTER_JS_CUBIC:
			{
				return (axisValue * axisValue * axisValue);
			}

			case FILTER_JS_NONE:
			default:
			{
				return axisValue;
			}
		}
	}

	public double getDPadX()
	{
		return gamepad.getRawAxis(DPAD_X);
	}

	public double getDPadY()
	{
		return gamepad.getRawAxis(DPAD_Y);
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

	public boolean getButtonA()
	{
		return gamepad.getRawButton(BTN_A);
	}

	public boolean getButtonB()
	{
		return gamepad.getRawButton(BTN_B);
	}

	public boolean getButtonX()
	{
		return gamepad.getRawButton(BTN_X);
	}

	public boolean getButtonY()
	{
		return gamepad.getRawButton(BTN_Y);
	}

	public boolean getButtonStart()
	{
		return gamepad.getRawButton(BTN_START);
	}

	public boolean getButtonSelect()
	{
		return gamepad.getRawButton(BTN_SELECT);
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

	public void logAxes()
	{
		SmartDashboard.putNumber("GamePad Axis 1", gamepad.getRawAxis(1));
		SmartDashboard.putNumber("GamePad Axis 2", gamepad.getRawAxis(2));
		SmartDashboard.putNumber("GamePad Axis 3", gamepad.getRawAxis(3));
		SmartDashboard.putNumber("GamePad Axis 4", gamepad.getRawAxis(4));
		SmartDashboard.putNumber("GamePad Axis 5", gamepad.getRawAxis(5));
		SmartDashboard.putNumber("GamePad Axis 6", gamepad.getRawAxis(6));
		SmartDashboard.putNumber("GamePad Axis 7", gamepad.getRawAxis(7));
		SmartDashboard.putNumber("GamePad Axis 8", gamepad.getRawAxis(8));
		SmartDashboard.putNumber("GamePad Axis 9", gamepad.getRawAxis(9));
		SmartDashboard.putNumber("GamePad Axis 10", gamepad.getRawAxis(10));
		SmartDashboard.putNumber("GamePad Axis 11", gamepad.getRawAxis(11));
		SmartDashboard.putNumber("GamePad Axis 12", gamepad.getRawAxis(12));
	}

	public void logButtons()
	{
		SmartDashboard.putBoolean("GamePad Button 1", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 2", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 3", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 4", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 5", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 6", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 7", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 8", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 9", gamepad.getRawButton(1));
		SmartDashboard.putBoolean("GamePad Button 10", gamepad.getRawButton(1));
	}

	public double getFilteredAxis(int axis, int FILTER_TYPE)
	{
		double outputValue = 0.0;

		switch(axis)
		{
			case JS_LEFT_X:
				outputValue = getJoystickLeftX();
				break;

			case JS_LEFT_Y:
				outputValue = getJoystickLeftY();
				break;

			case JS_RIGHT_X:
				outputValue = getJoystickRightX();
				break;

			case JS_RIGHT_Y:
				outputValue = getJoystickRightY();
				break;

			default:
				outputValue = 0.0;
				break;
		}

		switch(FILTER_TYPE)
		{
			case FILTER_JS_CUBIC:
				outputValue = outputValue * outputValue * outputValue;
				break;

			case FILTER_JS_SQUARED:
				outputValue = outputValue * outputValue;

				if(outputValue < 0.0)
				{
					outputValue = -(outputValue);
				}
				break;
		}

		return outputValue;
	}

	public double getFilteredJoystickLeftMagnitude(int FILTER_TYPE)
	{
		return Math.sqrt(Math.pow(getFilteredAxis(JS_LEFT_X, FILTER_TYPE), 2) + Math.pow(getFilteredAxis(JS_LEFT_Y, FILTER_TYPE), 2));
	}

	public double getFilteredJoystickRightMagnitude(int FILTER_TYPE)
	{
		return Math.sqrt(Math.pow(getFilteredAxis(JS_RIGHT_X, FILTER_TYPE), 2) + Math.pow(getFilteredAxis(JS_RIGHT_Y, FILTER_TYPE), 2));
	}

	public double getFilteredJoystickLeftDirectionRadians(int FILTER_TYPE)
	{
		return Math.atan2(getFilteredAxis(JS_LEFT_X, FILTER_TYPE), -getFilteredAxis(JS_LEFT_Y, FILTER_TYPE));
	}

	public double getFilteredJoystickLeftDirectionDegrees(int FILTER_TYPE)
	{
		return Math.toDegrees(getFilteredJoystickLeftDirectionRadians(FILTER_TYPE));
	}

	public double getFilteredJoystickRightDirectionRadians(int FILTER_TYPE)
	{
		return Math.atan2(getFilteredAxis(JS_RIGHT_X, FILTER_TYPE), -getFilteredAxis(JS_RIGHT_Y, FILTER_TYPE));
	}

	public double getFilteredJoystickRightDirectionDegrees(int FILTER_TYPE)
	{
		return Math.toDegrees(getFilteredJoystickRightDirectionRadians(FILTER_TYPE));
	}
}
