package org.sharp.frc.team3260.RecycleRush.subsystems;

import com.kauailabs.navx_mxp.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.SHARPPressureTransducer;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class DriveTrain extends SHARPSubsystem
{
    protected static DriveTrain instance;

    private static final int numDriveMotors = 4;

    private CANTalon frontLeftTalon, frontRightTalon, backLeftTalon, backRightTalon;

    private Compressor compressor;
    private SHARPPressureTransducer transducer;

    private AHRS imu;

    private boolean firstIteration;

    protected double gyroOffset = 0.0;

    protected double rotationControllerOutput = 0.0;

    protected double rotationTarget = 0.0;
    protected boolean rotatingToTarget = false;

    protected PIDController driveRotationController;

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

        frontLeftTalon.reverseOutput(true);
        frontRightTalon.reverseOutput(false);
        backLeftTalon.reverseOutput(true);
        backRightTalon.reverseOutput(false);

        frontLeftTalon.reverseSensor(true);
        frontRightTalon.reverseSensor(true);
        backLeftTalon.reverseSensor(true);
        backRightTalon.reverseSensor(true);

        zeroEncoders();

        try
        {
            SerialPort serialPort = new SerialPort(57600, SerialPort.Port.kMXP);

            byte updateRateHz = 50;

            imu = new AHRS(serialPort, updateRateHz);
        }
        catch(Exception ex)
        {
            imu = null;
        }

        if(imu != null)
        {
            LiveWindow.addSensor("IMU", "Gyro", imu);

            driveRotationController = new PIDController(Constants.rotationControllerP.getDouble(), Constants.rotationControllerI.getDouble(), Constants.rotationControllerD.getDouble(), Constants.rotationControllerF.getDouble(), getIMUPIDSource(), output -> rotationControllerOutput = output);

            driveRotationController.setPercentTolerance(1);

            driveRotationController.setContinuous();

            driveRotationController.setInputRange(-180, 180);
        }

        firstIteration = true;
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
                frontLeftTalon.setPID(Constants.driveSpeedControllerP.getDouble(), Constants.driveSpeedControllerI.getDouble(), Constants.driveSpeedControllerD.getDouble(), 0, 0, Constants.driveSpeedControllerCloseLoopRampRate.getInt(), 0);
                frontLeftTalon.enableControl();

                frontRightTalon.changeControlMode(CANTalon.ControlMode.Speed);
                frontRightTalon.set(0.0);
                frontRightTalon.setProfile(1);
                frontRightTalon.setPID(Constants.driveSpeedControllerP.getDouble(), Constants.driveSpeedControllerI.getDouble(), Constants.driveSpeedControllerD.getDouble(), 0, 0, Constants.driveSpeedControllerCloseLoopRampRate.getInt(), 0);
                frontRightTalon.enableControl();

                backLeftTalon.changeControlMode(CANTalon.ControlMode.Speed);
                backLeftTalon.set(0.0);
                backLeftTalon.setProfile(1);
                backLeftTalon.setPID(Constants.driveSpeedControllerP.getDouble(), Constants.driveSpeedControllerI.getDouble(), Constants.driveSpeedControllerD.getDouble(), 0, 0, Constants.driveSpeedControllerCloseLoopRampRate.getInt(), 0);
                backLeftTalon.enableControl();

                backRightTalon.changeControlMode(CANTalon.ControlMode.Speed);
                backRightTalon.set(0.0);
                backRightTalon.setProfile(1);
                backRightTalon.setPID(Constants.driveSpeedControllerP.getDouble(), Constants.driveSpeedControllerI.getDouble(), Constants.driveSpeedControllerD.getDouble(), 0, 0, Constants.driveSpeedControllerCloseLoopRampRate.getInt(), 0);
                backRightTalon.enableControl();

                log.info("ControlMode changed to " + controlMode.name());
                break;

            case Position:
                frontLeftTalon.changeControlMode(CANTalon.ControlMode.Position);
                frontLeftTalon.set(0.0);
                frontLeftTalon.setProfile(0);
                frontLeftTalon.setPID(Constants.drivePositionControllerP.getDouble(), Constants.drivePositionControllerI.getDouble(), Constants.drivePositionControllerD.getDouble(), 0, 0, Constants.drivePositionControllerCloseLoopRampRate.getInt(), 0);
                frontLeftTalon.enableControl();

                frontRightTalon.changeControlMode(CANTalon.ControlMode.Position);
                frontRightTalon.set(0.0);
                frontRightTalon.setProfile(0);
                frontRightTalon.setPID(Constants.drivePositionControllerP.getDouble(), Constants.drivePositionControllerI.getDouble(), Constants.drivePositionControllerD.getDouble(), 0, 0, Constants.drivePositionControllerCloseLoopRampRate.getInt(), 0);
                frontRightTalon.enableControl();

                backLeftTalon.changeControlMode(CANTalon.ControlMode.Position);
                backLeftTalon.set(0.0);
                backLeftTalon.setProfile(0);
                backLeftTalon.setPID(Constants.drivePositionControllerP.getDouble(), Constants.drivePositionControllerI.getDouble(), Constants.drivePositionControllerD.getDouble(), 0, 0, Constants.drivePositionControllerCloseLoopRampRate.getInt(), 0);
                backLeftTalon.enableControl();

                backRightTalon.changeControlMode(CANTalon.ControlMode.Position);
                backRightTalon.set(0.0);
                backRightTalon.setProfile(0);
                backRightTalon.setPID(Constants.drivePositionControllerP.getDouble(), Constants.drivePositionControllerI.getDouble(), Constants.drivePositionControllerD.getDouble(), 0, 0, Constants.drivePositionControllerCloseLoopRampRate.getInt(), 0);
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
        setDefaultCommand(new SHARPMecanumDriveCommand());
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
        frontLeftTalon.set(-frontLeftOutput);
        frontRightTalon.set(frontRightOutput);
        backLeftTalon.set(-backLeftOutput);
        backRightTalon.set(backRightOutput);
    }

    public void setDriveMotors(double[] wheelSpeeds)
    {
        normalize(wheelSpeeds);

        setDriveMotors(wheelSpeeds[0], wheelSpeeds[1], wheelSpeeds[2], wheelSpeeds[3]);
    }

    public void stockMecanumDrive(double x, double y, double rotation, double gyroAngle)
    {
        mecanumDrive_Cartesian(x, y, rotation, gyroAngle, false);
    }

    public void stockMecanumDrive(double x, double y, double rotation, double gyroAngle, boolean useRotationPID)
    {
        mecanumDrive_Cartesian(x, y, rotation, gyroAngle, useRotationPID);
    }

    public void tankDrive(double leftOutput, double rightOutput)
    {
        frontLeftTalon.set(-leftOutput);
        frontRightTalon.set(rightOutput);
        backLeftTalon.set(-leftOutput);
        backRightTalon.set(rightOutput);
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

    private double getRotationPID(double rotationSpeed)
    {
        if(driveRotationController.getSetpoint() != rotationTarget && rotatingToTarget)
        {
            rotatingToTarget = false;

            driveRotationController.setSetpoint(getIMU().getYaw());
        }

        if(rotatingToTarget)
        {
            if(!driveRotationController.isEnable())
            {
                driveRotationController.enable();
            }

            if(driveRotationController.onTarget())
            {
                rotatingToTarget = false;

                return 0.0;
            }

            return rotationControllerOutput;
        }

        if(driveRotationController.isEnable())
        {
            if(Math.abs(rotationSpeed) >= Constants.rotationControllerThreshold.getDouble())
            {
                driveRotationController.disable();
            }
            else
            {
                return rotationControllerOutput;
            }
        }
        else
        {
            if(Math.abs(rotationSpeed) < Constants.rotationControllerThreshold.getDouble())
            {
                gyroOffset = getIMU().getYaw();
                driveRotationController.setSetpoint(gyroOffset);
                driveRotationController.enable();
            }
        }

        return rotationSpeed;
    }

    public boolean reachedRotationTarget()
    {
        return driveRotationController.onTarget();
    }

    public void setRotationTarget(double rotationTarget)
    {
        driveRotationController.reset();

        rotatingToTarget = true;

        if(driveRotationController.getSetpoint() != rotationTarget)
        {
            this.rotationTarget = rotationTarget;

            driveRotationController.setSetpoint(rotationTarget);
        }

        if(!driveRotationController.isEnable())
        {
            driveRotationController.enable();
        }
    }

    public AHRS getIMU()
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

        frontLeftTalon.set(-frontLeftTarget);
        frontRightTalon.set(frontRightTarget);
        backLeftTalon.set(-backLeftTarget);
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

        return talonOnTarget(frontLeftTalon, Constants.driveControllerOnTargetThreshold.getInt()) && talonOnTarget(backLeftTalon, Constants.driveControllerOnTargetThreshold.getInt()) && talonOnTarget(frontRightTalon, Constants.driveControllerOnTargetThreshold.getInt()) && talonOnTarget(backRightTalon, Constants.driveControllerOnTargetThreshold.getInt());
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

        for(int i = 1; i < wheelSpeeds.length; i++)
        {
            double temp = Math.abs(wheelSpeeds[i]);
            if(maxMagnitude < temp)
            {
                maxMagnitude = temp;
            }
        }

        if(maxMagnitude > 1.0)
        {
            for(int i = 0; i < wheelSpeeds.length; i++)
            {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
    }

    public double getPressure()
    {
        return transducer.getPressure();
    }
}