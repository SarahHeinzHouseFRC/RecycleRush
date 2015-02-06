package org.sharp.frc.team3260.RecycleRush.subsystems;

import com.kauailabs.nav6.frc.IMUAdvanced;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPDriveCommand;
import org.sharp.frc.team3260.RecycleRush.utils.Util;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

public class DriveTrain extends SHARPSubsystem
{
    protected static DriveTrain instance;

    private CANTalon frontLeftTalon, frontRightTalon, backLeftTalon, backRightTalon;
    private RobotDrive robotDrive;
    private Encoder frontLeftEncoder, frontRightEncoder, backLeftEncoder, backRightEncoder;

    private IMUAdvanced imu;

    protected double rotationControllerP = 0.01,
            rotationControllerI = 0.0,
            rotationControllerD = 0.0,
            rotationControllerF = 0.0;

    protected double gyroOffset = 0.0;

    protected double rotationControllerOutput = 0.0;

    protected static final double ROTATION_DEADBAND = 0.05;

    protected double rotationTarget = 0.0;
    protected boolean rotatingToTarget = false;

    protected PIDController rotationController;

    public DriveTrain()
    {
        super("DriveTrain");

        instance = this;

        frontLeftTalon = new CANTalon(Constants.driveFrontLeftTalonID.getInt());
        frontRightTalon = new CANTalon(Constants.driveFrontRightTalonID.getInt());
        backLeftTalon = new CANTalon(Constants.driveBackLeftTalonID.getInt());
        backRightTalon = new CANTalon(Constants.driveBackRightTalonID.getInt());

        frontLeftTalon.enableBrakeMode(true);
        frontRightTalon.enableBrakeMode(true);
        backLeftTalon.enableBrakeMode(true);
        backRightTalon.enableBrakeMode(true);

        robotDrive = new RobotDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
        robotDrive.setSafetyEnabled(true);
        robotDrive.setExpiration(0.1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, Constants.driveFrontLeftInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, Constants.driveBackLeftInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Constants.driveFrontRightInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, Constants.driveBackRightInverted.getInt() == 1);

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
        catch (Exception ex)
        {
            imu = null;
        }

        if (imu != null)
        {
            LiveWindow.addSensor("IMU", "Gyro", imu);

            rotationController = new PIDController(rotationControllerP, rotationControllerI, rotationControllerD, rotationControllerF, getIMUPIDSource(), output -> rotationControllerOutput = output);

            SmartDashboard.putData("Rotation Controller", rotationController);
        }
    }

    public void initDefaultCommand()
    {
        setDefaultCommand(new SHARPDriveCommand());
    }

    public void tankDrive(double leftAxis, double rightAxis)
    {
        robotDrive.tankDrive(leftAxis, rightAxis);
    }

    public void zeroGyro()
    {
        imu.zeroYaw();
    }

    public void stop()
    {
        robotDrive.tankDrive(0, 0);
    }

    public void setDriveMotors(double frontLeftOutput, double frontRightOutput, double backLeftOutput, double backRightOutput)
    {
        frontLeftTalon.set(frontLeftOutput);
        frontRightTalon.set(frontRightOutput);
        backLeftTalon.set(backLeftOutput);
        backRightTalon.set(backRightOutput);

        SmartDashboard.putNumber("Drive Front Left", frontLeftOutput);
        SmartDashboard.putNumber("Drive Front Right", frontRightOutput);
        SmartDashboard.putNumber("Drive Back Left", backLeftOutput);
        SmartDashboard.putNumber("Drive Back Right", backRightOutput);
    }

    /**
     * @param x         The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y         The speed that the robot should drive in the Y direction.
     *                  This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation  The rate of rotation for the robot that is completely independent of
     *                  the translation. [-1.0..1.0]
     * @param gyroAngle The current angle reading from the gyro.  Use this to implement field-oriented controls.
     */
    public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle)
    {
        rotation = getRotationPID(rotation);
        mecanumDrive_Cartesian0(x, y, rotation, gyroAngle);
    }

    /**
     * @param x        The speed that the robot should drive in the X direction. [-1.0..1.0]
     * @param y        The speed that the robot should drive in the Y direction.
     *                 This input is inverted to match the forward == -1.0 that joysticks produce. [-1.0..1.0]
     * @param rotation The rate of rotation for the robot that is completely independent of
     *                 the translation. [-1.0..1.0]
     */
    public void mecanumDrive_Cartesian(double x, double y, double rotation)
    {
        mecanumDrive_Cartesian(x, y, rotation, getIMU().getYaw() - gyroOffset);
    }

    private void mecanumDrive_Cartesian0(double x, double y, double rotation, double gyroAngle)
    {
        double rotated[] = Util.rotateVector(x, y, gyroAngle);
        x = rotated[0];
        y = rotated[1];

        double wheelSpeeds[] = new double[4];
        wheelSpeeds[0] = x + y + rotation;
        wheelSpeeds[1] = -x + y - rotation;
        wheelSpeeds[2] = -x + y + rotation;
        wheelSpeeds[3] = x + y - rotation;

        Util.normalize(wheelSpeeds);

        setDriveMotors(wheelSpeeds[0], wheelSpeeds[1], wheelSpeeds[2], wheelSpeeds[3]);
    }

    public void mecanumDrive_Orientation(double x, double y, double angle)
    {
        if (!rotationController.isEnable() || rotationController.getSetpoint() != angle)
        {
            rotationController.setSetpoint(angle);
            rotationController.enable();
        }

        mecanumDrive_Cartesian0(x, y, rotationControllerOutput, getIMU().getYaw());
    }

    public void mecanumDrive_Cartesian(GenericHID stick)
    {
        mecanumDrive_Cartesian(stick.getX(), stick.getY(), 0);
    }

    /**
     * @param magnitude The speed that the robot should drive in a given
     *                  direction.
     * @param direction the direction the robot should drive in degrees,
     *                  independent of rotation
     * @param rotation  The rate of rotation for the robot that is completely
     *                  independent of the magnitude or direction. [-1.0..1.0]
     */
    public void mecanumDrive_Polar(double magnitude, double direction, double rotation)
    {
        magnitude = Util.limit(magnitude) * Math.sqrt(2.0);

        double dirInRad = (direction + 45.0) * Math.PI / 180.0;
        double cosD = Math.cos(dirInRad);
        double sinD = Math.sin(dirInRad);

        double wheelSpeeds[] = new double[4];
        wheelSpeeds[0] = (sinD * magnitude + rotation);
        wheelSpeeds[1] = (cosD * magnitude - rotation);
        wheelSpeeds[2] = (cosD * magnitude + rotation);
        wheelSpeeds[3] = (sinD * magnitude - rotation);

        Util.normalize(wheelSpeeds);

        setDriveMotors(wheelSpeeds[0], wheelSpeeds[1], wheelSpeeds[2], wheelSpeeds[3]);
    }

    /**
     * Moves the robot sideways at the specified speed.
     *
     * @param speed The speed and direction to crab (negative = left, positive = right)
     */
    public void crab(double speed)
    {
        mecanumDrive_Cartesian(speed, 0, 0);
    }

    /**
     * Gets the corrected rotation speed based on the gyro heading and the
     * expected rate of rotation. If the rotation rate is above a threshold, the
     * gyro correction is turned off.
     *
     * @param rotationSpeed Desired rotation speed
     * @return rotationSpeed
     */
    private double getRotationPID(double rotationSpeed)
    {
        if (rotationController.isEnable())
        {
            if (Math.abs(rotationSpeed) >= ROTATION_DEADBAND)
            {
                rotationController.disable();
            }
            else
            {
                return rotationControllerOutput;
            }
        }
        else
        {
            if (Math.abs(rotationSpeed) < ROTATION_DEADBAND)
            {
                gyroOffset = getIMU().getYaw();
                rotationController.setSetpoint(gyroOffset);
                rotationController.enable();
            }
        }

        return rotationSpeed;
    }

    public void setRotationTarget(double rotationTarget)
    {
        this.rotationTarget = rotationTarget;

        rotationController.setSetpoint(rotationTarget);
    }

    public void setRotatingToTarget(boolean rotatingToTarget)
    {
        this.rotatingToTarget = rotatingToTarget;
    }

    public IMUAdvanced getIMU()
    {
        return imu;
    }

    public PIDSource getIMUPIDSource()
    {
        return imu;
    }

    public void resetEncoders()
    {
        frontLeftEncoder.reset();
        frontRightEncoder.reset();
        backLeftEncoder.reset();
        backRightEncoder.reset();
    }

    public double getAverageEncoderDistance()
    {
        return Util.mean(new double[]{frontLeftEncoder.getDistance(), frontRightEncoder.getDistance(), backRightEncoder.getDistance(), backRightEncoder.getDistance()});
    }

    public static DriveTrain getInstance()
    {
        if (instance == null)
        {
            System.out.println("Something has gone horribly wrong.");
        }

        return instance;
    }
}