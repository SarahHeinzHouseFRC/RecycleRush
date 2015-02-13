package org.sharp.frc.team3260.RecycleRush.subsystems;

import com.kauailabs.nav6.frc.IMUAdvanced;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPDriveCommand;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class DriveTrain extends SHARPSubsystem
{
    protected static DriveTrain instance;

    public static final double THEORETICAL_MAX_SPEED = 11.82; // (ft/s)

    private CANTalon frontLeftTalon, frontRightTalon, backLeftTalon, backRightTalon;
    private RobotDrive robotDrive;

    private Compressor compressor;

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

        compressor = new Compressor(0);
        compressor.start();

        frontLeftTalon = new CANTalon(Constants.driveFrontLeftTalonID.getInt());
        frontRightTalon = new CANTalon(Constants.driveFrontRightTalonID.getInt());
        backLeftTalon = new CANTalon(Constants.driveBackLeftTalonID.getInt());
        backRightTalon = new CANTalon(Constants.driveBackRightTalonID.getInt());

        frontLeftTalon.enableBrakeMode(true);
        frontRightTalon.enableBrakeMode(true);
        backLeftTalon.enableBrakeMode(true);
        backRightTalon.enableBrakeMode(true);

        robotDrive = new RobotDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
        robotDrive.setSafetyEnabled(false);
        robotDrive.setExpiration(0.1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, Constants.driveFrontLeftInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, Constants.driveBackLeftInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Constants.driveFrontRightInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, Constants.driveBackRightInverted.getInt() == 1);

        //        frontLeftEncoder.setDistancePerPulse((4.0/*in*/ * Math.PI) / (256.0 * 12.0/*in/ft*/));

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

    public void changeControlMode(CANTalon.ControlMode controlMode)
    {
        if (controlMode == CANTalon.ControlMode.PercentVbus)
        {
            frontLeftTalon.disableControl();
            frontLeftTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
            frontLeftTalon.set(0.0);

            frontRightTalon.disableControl();
            frontRightTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
            frontRightTalon.set(0.0);

            backLeftTalon.disableControl();
            backLeftTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
            backLeftTalon.set(0.0);

            backRightTalon.disableControl();
            backRightTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
            backRightTalon.set(0.0);

            log.info("ControlMode changed to " + controlMode.getClass().getSimpleName());
        }
        else if (controlMode == CANTalon.ControlMode.Speed)
        {
            frontLeftTalon.changeControlMode(CANTalon.ControlMode.Speed);
            frontLeftTalon.set(0.0);
            frontLeftTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            frontLeftTalon.setPID(0.01, 0, 0, 0, 0, 12, 0);
            frontLeftTalon.enableControl();

            frontRightTalon.changeControlMode(CANTalon.ControlMode.Speed);
            frontRightTalon.set(0.0);
            frontRightTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            frontRightTalon.setPID(0.01, 0, 0, 0, 0, 12, 0);
            frontRightTalon.enableControl();

            backLeftTalon.changeControlMode(CANTalon.ControlMode.Speed);
            backLeftTalon.set(0.0);
            backLeftTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            backLeftTalon.setPID(0.01, 0, 0, 0, 0, 12, 0);
            backLeftTalon.enableControl();

            backRightTalon.changeControlMode(CANTalon.ControlMode.Speed);
            backRightTalon.set(0.0);
            backRightTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            backRightTalon.setPID(0.01, 0, 0, 0, 0, 12, 0);
            backRightTalon.enableControl();

            log.info("ControlMode changed to " + controlMode.getClass().getSimpleName());
        }
        else
        {
            log.warn("Invalid ControlMode supplied, not setting ControlMode to " + controlMode.getClass().getSimpleName());
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
        frontLeftTalon.set(frontLeftOutput * (Constants.driveFrontLeftInverted.getInt() == 1 ? -1 : 0));
        frontRightTalon.set(frontRightOutput * (Constants.driveFrontRightInverted.getInt() == 1 ? -1 : 0));
        backLeftTalon.set(backLeftOutput * (Constants.driveBackLeftInverted.getInt() == 1 ? -1 : 0));
        backRightTalon.set(backRightOutput * (Constants.driveBackRightInverted.getInt() == 1 ? -1 : 0));

        SmartDashboard.putNumber("Drive Front Left", frontLeftOutput * (Constants.driveFrontLeftInverted.getInt() == 1 ? -1 : 0));
        SmartDashboard.putNumber("Drive Front Right", frontRightOutput * (Constants.driveFrontRightInverted.getInt() == 1 ? -1 : 0));
        SmartDashboard.putNumber("Drive Back Left", backLeftOutput * (Constants.driveBackLeftInverted.getInt() == 1 ? -1 : 0));
        SmartDashboard.putNumber("Drive Back Right", backRightOutput * (Constants.driveBackRightInverted.getInt() == 1 ? -1 : 0));
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

        log.info(x + ", " + y + ", " + rotation);
        log.info(wheelSpeeds[0] + ", " + wheelSpeeds[1] + ", " + wheelSpeeds[2] + ", " + wheelSpeeds[3]);

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
        if (rotatingToTarget)
        {
            //            if (!rotationController.isEnable())
            //            {
            //                rotationController.enable();
            //            }
            //
            //            if (rotationController.getSetpoint() != rotationTarget)
            //            {
            //                rotationController.setSetpoint(rotationTarget);
            //            }
            //
            //            return rotationControllerOutput;
            rotatingToTarget = false;
        }

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

    public boolean reachedRotationTarget()
    {
        return rotationController.onTarget();
    }

    public void setRotationTarget(double rotationTarget)
    {
        rotatingToTarget = true;

        if (rotationController.getSetpoint() != rotationTarget)
        {
            this.rotationTarget = rotationTarget;

            rotationController.setSetpoint(rotationTarget);
        }

        if (!rotationController.isEnable())
        {
            rotationController.enable();
        }
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
        //        frontLeftEncoder.reset();
        //        frontRightEncoder.reset();
        //        backLeftEncoder.reset();
        //        backRightEncoder.reset();
    }

    public double getAverageEncoderDistance()
    {
        return 0;
        //        return Util.mean(new double[]{frontLeftEncoder.getDistance(), frontRightEncoder.getDistance(), backRightEncoder.getDistance(), backRightEncoder.getDistance()});
    }

    public static DriveTrain getInstance()
    {
        if (instance == null)
        {
            Robot.getInstance().getLogger().error("Something has gone horribly wrong in " + DriveTrain.class.getSimpleName());
        }

        return instance;
    }
}