package org.sharp.frc.team3260.RecycleRush.joystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Eddie
 */
public class XBoxGamepad extends Joystick
{
    public static final int JS_LEFT_X = 1,
            JS_LEFT_Y = 2,
            JS_RIGHT_X = 4,
            JS_RIGHT_Y = 5,
            JS_TRIGGERS = 3;

    private static final int DPAD_X = 6,
            DPAD_Y = 8;

    public static final int BTN_A = 1,
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
        super(id);
    }

    public double getJoystickLeftX()
    {
        return getRawAxis(JS_LEFT_X);
    }

    public double getJoystickLeftY()
    {
        return -getRawAxis(JS_LEFT_Y);
    }

    public double getJoystickRightX()
    {
        return getRawAxis(JS_RIGHT_X);
    }

    public double getJoystickRightY()
    {
        return -getRawAxis(JS_RIGHT_Y);
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
        switch (filter)
        {
            case FILTER_JS_SQUARED:
            {
                if (axisValue > 0)
                {
                    return (axisValue * axisValue);
                }
                else
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
        return getRawAxis(DPAD_X);
    }

    public double getDPadY()
    {
        return getRawAxis(DPAD_Y);
    }

    public boolean getButtonLeftBumper()
    {
        return getRawButton(BTN_LEFT_BUMPER);
    }

    public boolean getButtonRightBumper()
    {
        return getRawButton(BTN_RIGHT_BUMPER);
    }

    public boolean getButtonLeftJoystick()
    {
        return getRawButton(BTN_JS_LEFT);
    }

    public boolean getButtonRightJoystick()
    {
        return getRawButton(BTN_JS_RIGHT);
    }

    public boolean getButtonA()
    {
        return getRawButton(BTN_A);
    }

    public boolean getButtonB()
    {
        return getRawButton(BTN_B);
    }

    public boolean getButtonX()
    {
        return getRawButton(BTN_X);
    }

    public boolean getButtonY()
    {
        return getRawButton(BTN_Y);
    }

    public boolean getButtonStart()
    {
        return getRawButton(BTN_START);
    }

    public boolean getButtonSelect()
    {
        return getRawButton(BTN_SELECT);
    }

    public double getTriggerValue()
    {
        return -getRawAxis(JS_TRIGGERS);
    }

    public boolean getRightTrigger()
    {
        if (getTriggerValue() >= TRIGGER_THRESHOLD)
        {
            return true;
        }

        return false;
    }

    public boolean getLeftTrigger()
    {
        if (getTriggerValue() <= -TRIGGER_THRESHOLD)
        {
            return true;
        }

        return false;
    }

    public void logAxes()
    {
        SmartDashboard.putNumber("GamePad Axis 1", getRawAxis(1));
        SmartDashboard.putNumber("GamePad Axis 2", getRawAxis(2));
        SmartDashboard.putNumber("GamePad Axis 3", getRawAxis(3));
        SmartDashboard.putNumber("GamePad Axis 4", getRawAxis(4));
        SmartDashboard.putNumber("GamePad Axis 5", getRawAxis(5));
        SmartDashboard.putNumber("GamePad Axis 6", getRawAxis(6));
        SmartDashboard.putNumber("GamePad Axis 7", getRawAxis(7));
        SmartDashboard.putNumber("GamePad Axis 8", getRawAxis(8));
        SmartDashboard.putNumber("GamePad Axis 9", getRawAxis(9));
        SmartDashboard.putNumber("GamePad Axis 10", getRawAxis(10));
        SmartDashboard.putNumber("GamePad Axis 11", getRawAxis(11));
        SmartDashboard.putNumber("GamePad Axis 12", getRawAxis(12));
    }

    public void logButtons()
    {
        SmartDashboard.putBoolean("GamePad Button 1", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 2", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 3", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 4", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 5", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 6", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 7", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 8", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 9", getRawButton(1));
        SmartDashboard.putBoolean("GamePad Button 10", getRawButton(1));
    }

    public double getFilteredAxis(int axis, int FILTER_TYPE)
    {
        double outputValue = 0.0;

        switch (axis)
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

        switch (FILTER_TYPE)
        {
            case FILTER_JS_CUBIC:
                outputValue = outputValue * outputValue * outputValue;
                break;

            case FILTER_JS_SQUARED:
                outputValue = outputValue * outputValue;

                if (outputValue < 0.0)
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
