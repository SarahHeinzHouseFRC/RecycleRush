package org.sharp.frc.team3260.RecycleRush;

import org.sharp.frc.team3260.RecycleRush.utils.ConstantsBase;

public class Constants extends ConstantsBase
{
    public static final Constant mainGamepadID = new Constant("mainGamepadID", 1);
    public static final Constant manipulatorGamepadID = new Constant("manipulatorGamepadID", 2);

    public static final Constant gripperSolenoidForwardChannel = new Constant("gripperSolenoidForwardChannel", 0);
    public static final Constant gripperSolenoidReverseChannel = new Constant("gripperSolenoidReverseChannel", 0);

    public static final Constant aidanvatorCIM = new Constant("aidanvatorCIM", 0);

    static
    {
        readConstantsFromFile();
    }

    private Constants()
    {
    }
}