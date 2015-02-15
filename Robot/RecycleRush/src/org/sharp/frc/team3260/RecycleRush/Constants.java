package org.sharp.frc.team3260.RecycleRush;

import org.sharp.frc.team3260.RecycleRush.utils.ConstantsBase;

public class Constants extends ConstantsBase
{
    public static final Constant mainGamepadID = new Constant("mainGamepadID", 0);
    public static final Constant manipulatorGamepadID = new Constant("manipulatorGamepadID", 1);

    public static final Constant driveFrontLeftTalonID = new Constant("driveFrontLeftTalonID", 4);
    public static final Constant driveFrontRightTalonID = new Constant("driveFrontRightTalonID", 3);
    public static final Constant driveBackLeftTalonID = new Constant("driveBackLeftTalonID", 2);
    public static final Constant driveBackRightTalonID = new Constant("driveBackRightTalonID", 1);

    public static final Constant driveFrontLeftInverted = new Constant("driveFrontLeftInverted", 0);
    public static final Constant driveFrontRightInverted = new Constant("driveFrontRightInverted", 1);
    public static final Constant driveBackLeftInverted = new Constant("driveBackLeftInverted", 0);
    public static final Constant driveBackRightInverted = new Constant("driveBackRightInverted", 1);

    public static final Constant gripperSolenoidForwardChannel = new Constant("gripperSolenoidForwardChannel", 1);
    public static final Constant gripperSolenoidReverseChannel = new Constant("gripperSolenoidReverseChannel", 0);

    public static final Constant elevatorTalonID = new Constant("elevatorTalonID", 5);

    static
    {
        readConstantsFromFile();
    }

    private Constants()
    {
    }
}