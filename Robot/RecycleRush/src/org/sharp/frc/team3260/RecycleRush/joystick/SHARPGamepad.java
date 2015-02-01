package org.sharp.frc.team3260.RecycleRush.joystick;

import edu.wpi.first.wpilibj.Joystick;

public class SHARPGamepad extends Joystick
{
    public static final int JOYSTICK_LEFT_X = 0,
            JOYSTICK_LEFT_Y = 1,
            JOYSTICK_RIGHT_X = 4,
            JOYSTICK_RIGHT_Y = 5;

    public static final int TRIGGER_LEFT_AXIS = 2,
            TRIGGER_RIGHT_AXS = 3;

    public static final int BUTTON_A = 0,
            BUTTON_B = 1,
            BUTTON_X = 2,
            BUTTON_Y = 3;

    public static final int BUTTON_LEFT_BUMPER = 4,
            BUTTON_RIGHT_BUMPER = 5;

    public static final int BUTTON_MULTITASK = 6,
            BUTTON_MENU = 7;

    public static final int BUTTON_LEFT_JOYSTICK = 8,
            BUTTON_RIGHT_JOYSTICK = 9;

    public SHARPGamepad(int port)
    {
        super(port);
    }

    public void rumbleYo()
    {
        setRumble(RumbleType.kLeftRumble, 0.5f);
        setRumble(RumbleType.kRightRumble, 0.5f);
    }
}
