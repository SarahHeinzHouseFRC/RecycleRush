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

    public static final Constant drivePositionControllerP = new Constant("drivePositionControllerP", 0.3);
    public static final Constant drivePositionControllerI = new Constant("drivePositionControllerI", 0.000);
    public static final Constant drivePositionControllerD = new Constant("drivePositionControllerD", 0.0);
    public static final Constant drivePositionControllerCloseLoopRampRate = new Constant("drivePositionControllerCloseLoopRampRate", 0);

    public static final Constant drivePositionController2P = new Constant("drivePositionController2P", 0.50);
    public static final Constant drivePositionController2I = new Constant("drivePositionController2I", 0.00);
    public static final Constant drivePositionController2D = new Constant("drivePositionController2D", 0.00);
    public static final Constant drivePositionController2CloseLoopRampRate = new Constant("drivePositionController2CloseLoopRampRate", 0);

    public static final Constant driveControllerOnTargetThreshold = new Constant("driveControllerOnTargetThreshold", 200);

    public static final Constant rotationControllerP = new Constant("rotationControllerP", 0.00575);
    public static final Constant rotationControllerI = new Constant("rotationControllerI", 0.0002);
    public static final Constant rotationControllerD = new Constant("rotationControllerD", 0.0001);
    public static final Constant rotationControllerF = new Constant("rotationControllerF", 0.0);

    public static final Constant rotationControllerThreshold = new Constant("rotationControllerThreshold", 0.05);

    public static final Constant joystickDeadband = new Constant("joystickDeadband", 0.05);

    public static final Constant elevatorArmSolenoidForwardChannel = new Constant("elevatorArmSolenoidForwardChannel", 1);
    public static final Constant elevatorArmsSolenoidReverseChannel = new Constant("elevatorArmsSolenoidReverseChannel", 0);

    public static final Constant lowerArmsSolenoidForwardChannel = new Constant("lowerArmsSolenoidForwardChannel", 2);
    public static final Constant lowerArmsSolenoidReverseChannel = new Constant("lowerArmsSolenoidReverseChannel", 3);

    public static final Constant elevatorTalonID = new Constant("elevatorTalonID", 5);

    public static final Constant elevatorPositionControllerP = new Constant("elevatorPositionControllerP", 1.2);
    public static final Constant elevatorPositionControllerI = new Constant("elevatorPositionControllerI", 0.0);
    public static final Constant elevatorPositionControllerD = new Constant("elevatorPositionControllerD", 0.001);

    public static final Constant elevatorControllerOnTargetThreshold = new Constant("elevatorControllerOnTargetThreshold", 100);

    public static final Constant liftInfraredSensorPort = new Constant("liftInfraredSensorPort", 2);
    public static final Constant groundInfraredSensorPort = new Constant("groundInfraredSensorPort", 1);

    static
    {
        readConstantsFromFile();
    }

    private Constants()
    {
    }
}