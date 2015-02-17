package org.sharp.frc.team3260.RecycleRush.subsystems;

import com.kauailabs.nav6.frc.IMUAdvanced;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.commands.FIRSTMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class DriveTrain extends SHARPSubsystem
{
    protected static DriveTrain instance;

    private CANTalon frontLeftTalon, frontRightTalon, backLeftTalon, backRightTalon;
    private RobotDrive robotDrive;

    private Compressor compressor;

    private IMUAdvanced imu;

    protected double rotationControllerP = 0.0065,
            rotationControllerI = 0.0,
            rotationControllerD = 0.01,
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

        frontLeftTalon = new CANTalon(Constants.driveFrontLeftTalonID.getInt(), 5);
        frontRightTalon = new CANTalon(Constants.driveFrontRightTalonID.getInt(), 5);
        backLeftTalon = new CANTalon(Constants.driveBackLeftTalonID.getInt(), 5);
        backRightTalon = new CANTalon(Constants.driveBackRightTalonID.getInt(), 5);

        frontLeftTalon.enableBrakeMode(true);
        frontRightTalon.enableBrakeMode(true);
        backLeftTalon.enableBrakeMode(true);
        backRightTalon.enableBrakeMode(true);

        frontLeftTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        frontRightTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        backLeftTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        backRightTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

        frontLeftTalon.reverseSensor(true);
        frontRightTalon.reverseSensor(true);
        backLeftTalon.reverseSensor(true);
        backRightTalon.reverseSensor(true);

        zeroEncoders();

        robotDrive = new RobotDrive(frontLeftTalon, backLeftTalon, frontRightTalon, backRightTalon);
        robotDrive.setSafetyEnabled(false);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, Constants.driveFrontLeftInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, Constants.driveBackLeftInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Constants.driveFrontRightInverted.getInt() == 1);
        robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, Constants.driveBackRightInverted.getInt() == 1);

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

            rotationController.setPercentTolerance(1);

            rotationController.setContinuous();

            rotationController.setInputRange(-180, 180);

            SmartDashboard.putData("Rotation Controller", rotationController);
        }
    }

    public void changeControlMode(CANTalon.ControlMode controlMode)
    {
        switch (controlMode)
        {
            case PercentVbus:
                frontLeftTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
                frontLeftTalon.set(0.0);

                frontRightTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
                frontRightTalon.set(0.0);

                backLeftTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
                backLeftTalon.set(0.0);

                backRightTalon.changeControlMode(CANTalon.ControlMode.PercentVbus);
                backRightTalon.set(0.0);

                log.info("ControlMode changed to " + controlMode.name());
                break;

            case Speed:
                frontLeftTalon.changeControlMode(CANTalon.ControlMode.Speed);
                frontLeftTalon.set(0.0);
                frontLeftTalon.setProfile(1);
                frontLeftTalon.setPID(0.93239296, 0, 0, 0, 0, 12, 0);
                frontLeftTalon.enableControl();

                frontRightTalon.changeControlMode(CANTalon.ControlMode.Speed);
                frontRightTalon.set(0.0);
                frontRightTalon.setProfile(1);
                frontRightTalon.setPID(0.93239296, 0, 0, 0, 0, 12, 0);
                frontRightTalon.enableControl();

                backLeftTalon.changeControlMode(CANTalon.ControlMode.Speed);
                backLeftTalon.set(0.0);
                backLeftTalon.setProfile(1);
                backLeftTalon.setPID(0.93239296, 0, 0, 0, 0, 12, 0);
                backLeftTalon.enableControl();

                backRightTalon.changeControlMode(CANTalon.ControlMode.Speed);
                backRightTalon.set(0.0);
                backRightTalon.setProfile(1);
                backRightTalon.setPID(0.93239296, 0, 0, 0, 0, 12, 0);
                backRightTalon.enableControl();

                log.info("ControlMode changed to " + controlMode.name());
                break;

            case Position:
                frontLeftTalon.changeControlMode(CANTalon.ControlMode.Position);
                frontLeftTalon.set(0.0);
                frontLeftTalon.setProfile(0);
                frontLeftTalon.setPID(0.875, 0, 0, 0, 0, 0, 0);
                frontLeftTalon.enableControl();

                frontRightTalon.changeControlMode(CANTalon.ControlMode.Position);
                frontRightTalon.set(0.0);
                frontLeftTalon.setProfile(0);
                frontRightTalon.setPID(0.875, 0, 0, 0, 0, 0, 0);
                frontRightTalon.enableControl();

                backLeftTalon.changeControlMode(CANTalon.ControlMode.Position);
                backLeftTalon.set(0.0);
                frontLeftTalon.setProfile(0);
                backLeftTalon.setPID(0.875, 0, 0, 0, 0, 0, 0);
                backLeftTalon.enableControl();

                backRightTalon.changeControlMode(CANTalon.ControlMode.Position);
                backRightTalon.set(0.0);
                frontLeftTalon.setProfile(0);
                backRightTalon.setPID(0.875, 0, 0, 0, 0, 0, 0);
                backRightTalon.enableControl();

                log.info("ControlMode changed to " + controlMode.name());
                break;

            default:
                log.warn("Unsupported ControlMode supplied, not setting ControlMode to " + controlMode.name());
                break;
        }
    }

    public void initDefaultCommand()
    {
        setDefaultCommand(new FIRSTMecanumDriveCommand());
    }

    public void tankDrive(double leftAxis, double rightAxis)
    {
        robotDrive.tankDrive(leftAxis, rightAxis);
    }

    public void zeroGyro()
    {
        imu.zeroYaw();
    }

    public void zeroEncoders()
    {
        frontLeftTalon.setPosition(0.0);
        frontRightTalon.setPosition(0.0);
        backLeftTalon.setPosition(0.0);
        backRightTalon.setPosition(0.0);
    }

    public void stop()
    {
        if (frontLeftTalon.getControlMode() == CANTalon.ControlMode.PercentVbus)
        {
            robotDrive.tankDrive(0, 0);
        }
        else
        {
            setDriveMotors(frontLeftTalon.getPosition(), frontRightTalon.getPosition(), backLeftTalon.getPosition(), backRightTalon.getPosition());
        }
    }

    public void setDriveMotors(double frontLeftOutput, double frontRightOutput, double backLeftOutput, double backRightOutput)
    {
        frontLeftTalon.set(frontLeftOutput * (Constants.driveFrontLeftInverted.getInt() == 1 ? -1 : 0));
        frontRightTalon.set(frontRightOutput * (Constants.driveFrontRightInverted.getInt() == 1 ? -1 : 0));
        backLeftTalon.set(backLeftOutput * (Constants.driveBackLeftInverted.getInt() == 1 ? -1 : 0));
        backRightTalon.set(backRightOutput * (Constants.driveBackRightInverted.getInt() == 1 ? -1 : 0));
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

    public void stockMecanumDrive(double x, double y, double rotation, double gyroAngle)
    {
        robotDrive.mecanumDrive_Cartesian(x, y, rotation, gyroAngle);
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

        stockMecanumDrive(x, y, rotation, 0);
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
            if (!rotationController.isEnable())
            {
                rotationController.enable();
            }

            if (rotationController.getSetpoint() != rotationTarget)
            {
                rotationController.setSetpoint(rotationTarget);
            }

            if(rotationController.onTarget())
            {
                rotatingToTarget = false;

                return 0.0;
            }

            return rotationControllerOutput;
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

    public void setDriveEncoderTargets(double frontLeftTarget, double frontRightTarget, double backLeftTarget, double backRightTarget)
    {
        changeControlMode(CANTalon.ControlMode.Position);

        frontLeftTalon.setPosition(0);
        frontRightTalon.setPosition(0);
        backLeftTalon.setPosition(0);
        backRightTalon.setPosition(0);

        frontLeftTalon.set(frontLeftTarget);
        frontRightTalon.set(frontRightTarget);
        backLeftTalon.set(backLeftTarget);
        backRightTalon.set(backRightTarget);

        log.info("Drive Encoder Targets: " + frontLeftTarget + ", " + frontRightTarget + ", " + backLeftTarget + ", " + backRightTarget);
    }

    public boolean atDriveTarget()
    {
        if (frontLeftTalon.getControlMode() != CANTalon.ControlMode.Position || frontRightTalon.getControlMode() != CANTalon.ControlMode.Position || backLeftTalon.getControlMode() != CANTalon.ControlMode.Position || backRightTalon.getControlMode() != CANTalon.ControlMode.Position)
        {
            log.warn("Checked atDriveTarget, but one of the Talons was set to the wrong mode.");

            return false;
        }

        return talonOnTarget(frontLeftTalon, 250) && talonOnTarget(backLeftTalon, 250) && talonOnTarget(frontRightTalon, 250) && talonOnTarget(backRightTalon, 250);
    }

    protected boolean talonOnTarget(CANTalon talon, double tolerance)
    {
        return (Math.abs(talon.getSetpoint() - talon.getPosition()) < tolerance);
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