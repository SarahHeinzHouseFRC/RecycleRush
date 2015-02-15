package org.sharp.frc.team3260.RecycleRush;

import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
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
    private CommandGroup autonomousCommandGroup;

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
        SmartDashboard.putData("Rotate To 180", new RotateToHeadingCommand(180, 5, false));
        SmartDashboard.putData("Zero Gyro", new ZeroGyroCommand());

        log.info("Loading Autonomous Commands from CSV...");
        autonomousCommandGroup = new ScriptedAutonomous();

        log.info("Attempting to start Camera Server...");
        try
        {
            CameraServer.getInstance().setQuality(50);
            CameraServer.getInstance().startAutomaticCapture("cam0");
        }
        catch(Exception e)
        {
            log.info("Starting Camera Server failed with exception " + e.getMessage());
        }
    }

    public void autonomousInit()
    {
        if (autonomousCommandGroup.getClass() == ScriptedAutonomous.class && ((ScriptedAutonomous) autonomousCommandGroup).commandWasSuccessFul())
        {
            log.info("Scripted Autonomous Loaded Successfully.");

            autonomousCommandGroup.start();
        }
        else
        {
            log.info("Scripted Autonomous Loading Failed.");

            BasicAutoCommandGroup basicAuto = new BasicAutoCommandGroup();

            basicAuto.start();

            autonomousCommandGroup = basicAuto;
        }
    }

    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();

        Elevator.getInstance().checkLimitSwitch();
    }

    public void teleopInit()
    {
    }

    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();

        OI.getInstance().checkControls();

        Elevator.getInstance().checkLimitSwitch();
    }

    public void testPeriodic()
    {
        LiveWindow.run();
    }

    public void disabledInit()
    {
        if (autonomousCommandGroup != null)
        {
            autonomousCommandGroup.cancel();
        }
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
}
