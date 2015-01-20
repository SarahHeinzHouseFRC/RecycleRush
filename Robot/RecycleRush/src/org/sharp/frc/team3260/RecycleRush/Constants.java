package org.sharp.frc.team3260.RecycleRush;

public class Constants   // This is placeholder for until we make a new constants file reader
{
	// Operator Interface
	public static final int driverPort = 0;
	public static final int operatorPort = 1;
	public static final int thirdWheelPort = 2;

	// Drive Settings
	public static final double driveMaxOutput = 1.0;
	public static final double driveWheelCircumference = 12.57;
	public static final int driveEncoderTicksPerRevolution = 256;

	// Drive CANTalon IDs
	public static final int driveMotorFrontLeft = 0;
	public static final int driveMotorRearLeft = 1;
	public static final int driveMotorFrontRight = 2;
	public static final int driveMotorRearRight = 3;

	// Drive Encoder Turn PID Gains
	public static final double driveEncoderTurnkP = 0;
	public static final double driveEncoderTurnkI = 0;
	public static final double driveEncoderTurnkD = 0;

	// Drive Gyroscope Turn PID Gains
	public static final double driveGyroscopeTurnkP = 0;
	public static final double driveGyroscopeTurnkI = 0;
	public static final double driveGyroscopeTurnkD = 0;

	// Drive Straight PID Gains
	public static final double driveStraightkP = 0;
	public static final double driveStraightkI = 0;
	public static final double driveStraightkD = 0;
}
