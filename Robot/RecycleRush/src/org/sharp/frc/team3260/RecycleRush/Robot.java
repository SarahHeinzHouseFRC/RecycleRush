package org.sharp.frc.team3260.RecycleRush;

import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

public class Robot extends IterativeRobot
{
    private ScriptedAutonomous autonomousCommandGroup;

    private static final Log log = new Log("RobotBase", Log.ATTRIBUTE_TIME);

    private static Robot instance;

    int session;

    NIVision.Image frame;

    boolean hasCamera = false;

    public Robot()
    {
        if (instance == null)
        {
            instance = this;
        }
        else
        {
            return;
        }
    }

    public void robotInit()
    {
        log.info("Creating subsystem instances...");
        new DriveTrain();
        new Elevator();
        new Gripper();

        log.info("Adding SmartDashboard buttons...");
        SmartDashboard.putData("SHARPDrive", new SHARPDriveCommand());
        SmartDashboard.putData("Field Centric Mecanum Drive", new FieldCentricMecanumDriveCommand());
        SmartDashboard.putData("FIRST Mecanum Drive", new FIRSTMecanumDriveCommand());
        SmartDashboard.putData("Rotate To 180", new RotateToHeadingCommand(180, 5, true));
        SmartDashboard.putData("Zero Gyro", new ZeroGyroCommand());

        try
        {
            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

            session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);

            NIVision.IMAQdxConfigureGrab(session);

            NIVision.IMAQdxStartAcquisition(session);

            hasCamera = true;
        }
        catch (Exception e)
        {
            hasCamera = false;
        }

        if (hasCamera)
        {
            Runnable cameraThread = this::sendImage;

            cameraThread.run();
        }
    }

    public void autonomousInit()
    {
        autonomousCommandGroup = new ScriptedAutonomous();

        if (autonomousCommandGroup.commandWasSuccessFul())
        {
            log.info("Scripted Autonomous Loaded Successfully.");

            autonomousCommandGroup.start();
        }
        else
        {
            log.info("Scripted Autonomous Loading Failed.");
            BasicAutoCommandGroup basicAuto = new BasicAutoCommandGroup();
            basicAuto.start();
        }
    }

    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();
    }

    public void teleopInit()
    {
    }

    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();

        OI.getInstance().checkControls();
    }

    public void testPeriodic()
    {
        LiveWindow.run();
    }

    public void disabledInit()
    {
    }

    public void disabledPeriodic()
    {
    }

    public Log getLogger()
    {
        return log;
    }

    public static Robot getInstance()
    {
        return instance;
    }

    public void sendImage()
    {
        NIVision.IMAQdxGrab(session, frame, 1);

        CameraServer.getInstance().setImage(frame);
    }
}
