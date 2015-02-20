package org.sharp.frc.team3260.RecycleRush.subsystems;

import com.kauailabs.nav6.frc.IMUAdvanced;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.SHARPPressureTransducer;
import org.sharp.frc.team3260.RecycleRush.commands.FIRSTMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class DriveTrain extends SHARPSubsystem
{
    protected static DriveTrain instance;

    private static final int numDriveMotors = 4;

    private CANTalon frontLeftTalon, frontRightTalon, backLeftTalon, backRightTalon;

    private Compressor compressor;
    private SHARPPressureTransducer transducer;

    private IMUAdvanced imu;

    protected double rotationControllerP = 0.00573,
            rotationControllerI = 0.0001,
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

        transducer = new SHARPPressureTransducer(0);

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

        frontLeftTalon.reverseOutput(Constants.driveBackRightInverted.getBoolean());

        frontLeftTalon.reverseSensor(true);
        frontRightTalon.reverseSensor(true);
        backLeftTalon.reverseSensor(true);
        backRightTalon.reverseSensor(true);

        zeroEncoders();

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

            rotationController = new PIDController(rotationControllerP, rotationControllerI, rotationControllerD, rotationControllerF, getIMUPIDSource(), output -> rotationControllerOutput = output);

            rotationController.setPercentTolerance(1);

            rotationController.setContinuous();

            rotationController.setInputRange(-180, 180);

            SmartDashboard.putData("Rotation Controller", rotationController);
        }
    }

    public void changeControlMode(CANTalon.ControlMode controlMode)
    {
        switch(controlMode)
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
                frontRightTalon.setProfile(0);
                frontRightTalon.setPID(0.875, 0, 0, 0, 0, 0, 0);
                frontRightTalon.enableControl();

                backLeftTalon.changeControlMode(CANTalon.ControlMode.Position);
                backLeftTalon.set(0.0);
                backLeftTalon.setProfile(0);
                backLeftTalon.setPID(0.875, 0, 0, 0, 0, 0, 0);
                backLeftTalon.enableControl();

                backRightTalon.changeControlMode(CANTalon.ControlMode.Position);
                backRightTalon.set(0.0);
                backRightTalon.setProfile(0);
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
        if(frontLeftTalon.getControlMode() == CANTalon.ControlMode.PercentVbus)
        {
            setDriveMotors(0, 0, 0, 0);
        }
        else
        {
            setDriveMotors(frontLeftTalon.getPosition(), frontRightTalon.getPosition(), backLeftTalon.getPosition(), backRightTalon.getPosition());
        }
    }

    public void setDriveMotors(double frontLeftOutput, double frontRightOutput, double backLeftOutput, double backRightOutput)
    {
        frontLeftTalon.set(frontLeftOutput);
        frontRightTalon.set(frontRightOutput);
        backLeftTalon.set(backLeftOutput);
        backRightTalon.set(backRightOutput);
    }

    public void setDriveMotors(double[] wheelSpeeds)
    {
        normalize(wheelSpeeds);
    }

    public void stockMecanumDrive(double x, double y, double rotation, double gyroAngle)
    {
        mecanumDrive_Cartesian(x, y, rotation, gyroAngle, false);
    }

    public void mecanumDrive_Cartesian(double x, double y, double rotation, double gyroAngle, boolean useRotationPID)
    {
        double xIn = x;
        double yIn = y;
        // Negate y for the joystick.
        yIn = -yIn;

        if(useRotationPID)
        {
            rotation = getRotationPID(rotation);
        }

        // Compenstate for gyro angle.
        double rotated[] = Util.rotateVector(xIn, yIn, gyroAngle);
        xIn = rotated[0];
        yIn = rotated[1];

        double wheelSpeeds[] = new double[numDriveMotors];
        wheelSpeeds[0] = xIn + yIn + rotation;
        wheelSpeeds[1] = -xIn + yIn - rotation;
        wheelSpeeds[2] = -xIn + yIn + rotation;
        wheelSpeeds[3] = xIn + yIn - rotation;

        setDriveMotors(wheelSpeeds);
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
        if(rotationController.getSetpoint() != rotationTarget && rotatingToTarget)
        {
            rotatingToTarget = false;

            rotationController.setSetpoint(getIMU().getYaw());
        }

        if(rotatingToTarget)
        {
            if(!rotationController.isEnable())
            {
                rotationController.enable();
            }

            if(rotationController.onTarget())
            {
                rotatingToTarget = false;

                return 0.0;
            }

            return rotationControllerOutput;
        }

        if(rotationController.isEnable())
        {
            if(Math.abs(rotationSpeed) >= ROTATION_DEADBAND)
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
            if(Math.abs(rotationSpeed) < ROTATION_DEADBAND)
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
        rotationController.reset();

        rotatingToTarget = true;

        if(rotationController.getSetpoint() != rotationTarget)
        {
            this.rotationTarget = rotationTarget;

            rotationController.setSetpoint(rotationTarget);
        }

        if(!rotationController.isEnable())
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
        if(frontLeftTalon.getControlMode() != CANTalon.ControlMode.Position || frontRightTalon.getControlMode() != CANTalon.ControlMode.Position || backLeftTalon.getControlMode() != CANTalon.ControlMode.Position || backRightTalon.getControlMode() != CANTalon.ControlMode.Position)
        {
            log.warn("Checked atDriveTarget, but one of the Talons was set to the wrong mode.");

            return false;
        }

        return talonOnTarget(frontLeftTalon, 200) && talonOnTarget(backLeftTalon, 200) && talonOnTarget(frontRightTalon, 200) && talonOnTarget(backRightTalon, 200);
    }

    protected boolean talonOnTarget(CANTalon talon, double tolerance)
    {
        return (Math.abs(talon.getSetpoint() - talon.getPosition()) < tolerance);
    }

    public void clearAccumulatedI()
    {
        frontLeftTalon.ClearIaccum();
        frontRightTalon.ClearIaccum();
        backLeftTalon.ClearIaccum();
        backRightTalon.ClearIaccum();
    }

    public void showPressure()
    {
        SmartDashboard.putNumber("Pressure", transducer.getPressure());
    }

    public static DriveTrain getInstance()
    {
        if(instance == null)
        {
            Robot.getInstance().getLogger().error("Something has gone horribly wrong in " + DriveTrain.class.getSimpleName());
        }

        return instance;
    }

    protected static void normalize(double wheelSpeeds[])
    {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        int i;
        for(i = 1; i < numDriveMotors; i++)
        {
            double temp = Math.abs(wheelSpeeds[i]);
            if(maxMagnitude < temp)
            {
                maxMagnitude = temp;
            }
        }
        if(maxMagnitude > 1.0)
        {
            for(i = 0; i < numDriveMotors; i++)
            {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
    }
}