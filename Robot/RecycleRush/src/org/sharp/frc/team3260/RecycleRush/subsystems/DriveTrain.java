package org.sharp.frc.team3260.RecycleRush.subsystems;

import com.kauailabs.nav6.frc.IMUAdvanced;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.sharp.frc.team3260.RecycleRush.commands.DriveWithJoystick;

/**
 * The DriveTrain subsystem controls the robot's chassis and reads in
 * information about it's speed and position.
 */
public class DriveTrain extends Subsystem
{
	// Subsystem devices
	private CANTalon frontLeft, frontRight, backLeft, backRight;
	private RobotDrive drive;
	private Encoder frontLeftEncoder, frontRightEncoder, backLeftEncoder, backRightEncoder;

	private IMUAdvanced imu;

	public DriveTrain()
	{
		frontLeft = new CANTalon(0);
		frontRight = new CANTalon(1);
		backLeft = new CANTalon(2);
		backRight = new CANTalon(3);

		drive = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
		drive.setSafetyEnabled(true);
		drive.setExpiration(0.1);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		frontLeftEncoder = new Encoder(1, 2, true, EncodingType.k4X);
		frontRightEncoder = new Encoder(3, 4, true, EncodingType.k4X);
		backLeftEncoder = new Encoder(5, 6, true, EncodingType.k4X);
		backRightEncoder = new Encoder(7, 8, true, EncodingType.k4X);

		frontLeftEncoder.setPIDSourceParameter(PIDSourceParameter.kDistance);
		frontRightEncoder.setPIDSourceParameter(PIDSourceParameter.kDistance);
		backLeftEncoder.setPIDSourceParameter(PIDSourceParameter.kDistance);
		backRightEncoder.setPIDSourceParameter(PIDSourceParameter.kDistance);

		frontLeftEncoder.setDistancePerPulse((4.0/*in*/ * Math.PI) / (256.0 * 12.0/*in/ft*/));
		backLeftEncoder.setDistancePerPulse((4.0/*in*/ * Math.PI) / (256.0 * 12.0/*in/ft*/));

		LiveWindow.addSensor("DriveTrain", "Front Left Encoder", frontLeftEncoder);
		LiveWindow.addSensor("DriveTrain", "Front Right Encoder", frontRightEncoder);
		LiveWindow.addSensor("DriveTrain", "Back Left Encoder", backLeftEncoder);
		LiveWindow.addSensor("DriveTrain", "Back Right Encoder", backRightEncoder);

		try
		{
			SerialPort serialPort = new SerialPort(57600, SerialPort.Port.kOnboard);

			byte updateRateHz = 50;

			imu = new IMUAdvanced(serialPort, updateRateHz);
		}
		catch(Exception ex)
		{
			imu = null;
		}

		if(imu != null)
		{
			LiveWindow.addSensor("IMU", "Gyro", imu);
		}
	}

	/**
	 * When other commands aren't using the drivetrain, allow tank drive with
	 * the joystick.
	 */
	public void initDefaultCommand()
	{
		setDefaultCommand(new DriveWithJoystick());
	}

	/**
	 * @param joy PS3 style joystick to use as the input for tank drive.
	 */
	public void tankDrive(Joystick joy)
	{
		drive.tankDrive(joy.getY(), joy.getRawAxis(4));
	}

	/**
	 * @param leftAxis  Left sides value
	 * @param rightAxis Right sides value
	 */
	public void tankDrive(double leftAxis, double rightAxis)
	{
		drive.tankDrive(leftAxis, rightAxis);
	}

	/**
	 * Stop the drivetrain from moving.
	 */
	public void stop()
	{
		drive.tankDrive(0, 0);
	}
}