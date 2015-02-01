package org.sharp.frc.team3260.RecycleRush;

import org.sharp.frc.team3260.RecycleRush.utils.ConstantsBase;

public class Constants extends ConstantsBase
{
    public static final Constant mainGamepadID = new Constant("mainGamepadID", 1);
    public static final Constant manipulatorGamepadID = new Constant("manipulatorGamepadID", 2);

    public static final Constant driveFrontLeftPort = new Constant("driveFrontLeftPort", 0);
    public static final Constant driveFrontRightPort = new Constant("driveFrontRightPort", 1);
    public static final Constant driveBackLeftPort = new Constant("driveBackLeftPort", 2);
    public static final Constant driveBackRightPort = new Constant("driveBackRightPort", 3);

    public static final Constant driveFrontLeftInverted = new Constant("driveFrontLeftInverted", 1);
    public static final Constant driveFrontRightInverted = new Constant("driveFrontRightInverted", 1);
    public static final Constant driveBackLeftInverted = new Constant("driveFrontRightInverted", 1);
    public static final Constant driveBackRightInverted = new Constant("driveFrontRightInverted", 1);

    public static final Constant gripperSolenoidForwardChannel = new Constant("gripperSolenoidForwardChannel", 0);
    public static final Constant gripperSolenoidReverseChannel = new Constant("gripperSolenoidReverseChannel", 1);

    public static final Constant elevatorCIM = new Constant("elevatorCIM", 4);

    public static final Constant intakeWheelLeft = new Constant("intakeWheelLeft", 7);
    public static final Constant intakeWheelRight = new Constant("intakeWheelRight", 8);

    static
    {
        readConstantsFromFile();
    }

    private Constants()
    {
    }
}