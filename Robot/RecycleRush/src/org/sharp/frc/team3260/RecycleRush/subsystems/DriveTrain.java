package org.sharp.frc.team3260.RecycleRush.subsystems;

import com.kauailabs.nav6.frc.IMUAdvanced;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.commands.FieldCentricMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class DriveTrain extends Subsystem
{
    private RobotDrive drive;
    private CANTalon frontLeft, frontRight, backLeft, backRight;
    private Encoder frontLeftEncoder, frontRightEncoder, backLeftEncoder, backRightEncoder;

    private IMUAdvanced imu;

    protected double rotationControllerP = 0.01,
            rotationControllerI = 0.0,
            rotationControllerD = 0.0,
            rotationControllerF = 0.0;

    protected double gyroOffset = 0.0;

    protected double rotationControllerOutput = 0.0;

    protected double ROTATION_DEADBAND = 0.05;

    protected double rotationTarget = 0.0;
    protected boolean rotatingToTarget = false;

    protected PIDController rotationController;

    public DriveTrain()
    {
        frontLeft = new CANTalon(Constants.driveFrontLeftPort.getInt());
        frontRight = new CANTalon(Constants.driveFrontRightPort.getInt());
        backLeft = new CANTalon(Constants.driveBackLeftPort.getInt());
        backRight = new CANTalon(Constants.driveBackRightPort.getInt());

        drive = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
        drive.setSafetyEnabled(true);
        drive.setExpiration(0.1);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, Constants.driveFrontLeftInverted.getInt() == 1);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, Constants.driveBackLeftInverted.getInt() == 1);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, Constants.driveFrontRightInverted.getInt() == 1);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, Constants.driveBackRightInverted.getInt() == 1);

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

            rotationController = new PIDController(rotationControllerP, rotationControllerI, rotationControllerD, rotationControllerF, getIMUPIDSource(), new PIDOutput()
            {
                @Override
                public void pidWrite(double output)
                {
                    rotationControllerOutput = output;
                }
            });

            SmartDashboard.putData("Rotation Controller", rotationController);
        }
    }

    /**
     * When other commands aren't using the drivetrain, allow tank drive with
     * the joystick.
     */
    public void initDefaultCommand()
    {
        setDefaultCommand(new FieldCentricMecanumDriveCommand());
    }

    @Override
    protected void log()
    {
        SmartDashboard.putNumber("IMU Gyro", imu.getYaw());
    }

    public void tankDrive(Joystick joy)
    {
        drive.tankDrive(joy.getY(), joy.getRawAxis(4));
    }

    public void tankDrive(double leftAxis, double rightAxis)
    {
        drive.tankDrive(leftAxis, rightAxis);
    }

    public void zeroGyro()
    {
        imu.zeroYaw();
    }

    public void stop()
    {
        drive.tankDrive(0, 0);
    }

    public void setDriveMotors(double frontLeftOutput, double frontRightOutput, double backLeftOutput, double backRightOutput)
    {
        frontLeft.set(frontLeftOutput);
        frontRight.set(frontRightOutput);
        backLeft.set(backLeftOutput);
        backRight.set(backRightOutput);
    }

    /**
     * Moves the robot forward and sideways while rotating at the specified
     * speeds. This method uses the specified angle to implement field oriented
     * controls.
     *
     * @param x         The forward speed (negative = backward, positive = forward)
     * @param y         The sideways (crab) speed (negative = left, positive = right)
     * @param rotation  The speed to rotate at while moving (negative =
     *                  clockwise, positive = counterclockwise)
     * @param gyroAngle the current angle reading from the gyro
     */
    public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle)
    {
        rotation = getRotationPID(rotation);
        mecanumDrive_Cartesian0(x, y, rotation, gyroAngle);
    }

    /**
     * Moves the robot forward and sideways while rotating at the specified
     * speeds. This moves the robot relative to the robot's current orientation.
     *
     * @param x        The forward speed (negative = backward, positive = forward)
     * @param y        The sideways (crab) speed (negative = left, positive = right)
     * @param rotation The speed to rotate at while moving (negative =
     *                 clockwise, positive = counterclockwise)
     */
    public void mecanumDrive_Cartesian(double x, double y, double rotation)
    {
        mecanumDrive_Cartesian(x, y, rotation, Robot.getDrivetrain().getIMU().getYaw() - gyroOffset);
    }

    public void mecanumDrive_Orientation(double x, double y, double angle)
    {
        if (!rotationController.isEnable() || rotationController.getSetpoint() != angle)
        {
            rotationController.setSetpoint(angle);
            rotationController.enable();
        }

        mecanumDrive_Cartesian0(x, y, rotationControllerOutput, Robot.getDrivetrain().getIMU().getYaw());
    }

    /**
     * Real implementation of cartesian mecanum driving. This method does not
     * include gyro orientation correction and it is only called from with this
     * class.
     *
     * @param x         The forward speed (negative = backward, positive = forward)
     * @param y         The sideways (crab) speed (negative = left, positive = right)
     * @param rotation  The speed to rotate at while moving (positive =
     *                  clockwise, negative = counterclockwise)
     * @param gyroAngle the current angle reading from the gyro
     */
    private void mecanumDrive_Cartesian0(double x, double y, double rotation, double gyroAngle)
    {
        // Send debugging values.
        SmartDashboard.putNumber("Mecanum X", x);
        SmartDashboard.putNumber("Mecanum Y", y);
        SmartDashboard.putNumber("Mecanum Rotation", rotation);

        // Compenstate for gyro angle.
        double rotated[] = Util.rotateVector(x, y, gyroAngle);
        x = rotated[0];
        y = rotated[1];

        double wheelSpeeds[] = new double[4];
        wheelSpeeds[0] = x + y + rotation; // Front left speed
        wheelSpeeds[1] = -x + y - rotation; // Front right speed
        wheelSpeeds[2] = -x + y + rotation; // Rear left speed
        wheelSpeeds[3] = x + y - rotation; // Rear right speed

        Util.normalize(wheelSpeeds);

        setDriveMotors(wheelSpeeds[0], wheelSpeeds[1], wheelSpeeds[2], wheelSpeeds[3]);
    }

    /**
     * Drives the robot at the specified speed in the direction specified as an
     * angle in degrees while rotating. This does not take into account the gyro
     * correction yet because we do not use it.
     *
     * @param magnitude The speed that the robot should drive in a given
     *                  direction.
     * @param direction the direction the robot should drive in degrees,
     *                  independent of rotation
     * @param rotation  The rate of rotation for the robot that is completely
     *                  independent of the magnitude or direction. [-1.0..1.0]
     */
    public void mecanumDrive_Polar(double magnitude, double direction, double rotation)
    {
        // Normalized for full power along the Cartesian axes.
        magnitude = Util.limit(magnitude) * Math.sqrt(2.0);
        // The rollers are at 45 degree angles.
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
     * Drive based on the specified joystick using the x and y axes.
     *
     * @param stick The joystick to use
     */
    public void mecanumDrive_Cartesian(GenericHID stick)
    {
        mecanumDrive_Cartesian(stick.getX(), stick.getY(), 0);
    }

    /**
     * Moves the robot sideways at the specified speed.
     *
     * @param speed The speed and direction to crab (negative = left, positive =
     *              right)
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
     * @param rotationSpeed
     * @return rotationSpeed
     */
    private double getRotationPID(double rotationSpeed)
    {
        // If the controller is already enabled, check to see if it should be
        // disabled  or kept running. Otherwise check to see if it needs to be
        // enabled.
        if (rotationController.isEnable())
        {
            // If the rotation rate is greater than the deadband disable the PID
            // controller. Otherwise, return the latest value from the
            // controller.
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
            // If the rotation rate is less than the deadband, turn on the PID
            // controller and set its setpoint to the current angle.
            if (Math.abs(rotationSpeed) < ROTATION_DEADBAND)
            {
                gyroOffset = Robot.getDrivetrain().getIMU().getYaw();
                rotationController.setSetpoint(gyroOffset);
                rotationController.enable();
            }
        }
        // Unless told otherwise, return the rate that was passed in.
        return rotationSpeed;
    }

    public void setRotationTarget(double rotationTarget)
    {
        this.rotationTarget = rotationTarget;
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
}