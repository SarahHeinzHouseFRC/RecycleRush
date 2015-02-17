package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.autonomous.ScriptedAutonomous;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

import java.io.File;

public class Robot extends IterativeRobot
{
    private static final Log log = new Log("RobotBase", Log.ATTRIBUTE_TIME);

    private static Robot instance;

    private ScriptedAutonomous scriptedAutonomous;
    private SendableChooser autoChooser;

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
        SmartDashboard.putData("Zero Gyro", new ZeroGyroCommand());

        log.info("Indexing SmartDashbord autonomous options...");
        File[] listOfAutoFiles = new File("//home//lvuser//autonomous").listFiles();

        autoChooser.addDefault(listOfAutoFiles[0].getName(),new ScriptedAutonomous(listOfAutoFiles[0].getName()));
        for (File autoOption : listOfAutoFiles){
            autoChooser.addObject(autoOption.getName(), new ScriptedAutonomous(autoOption.getName()));
        }
        SmartDashboard.putData("Auto Chooser",autoChooser);

        log.info("Attempting to start Camera Server...");
        try
        {
            CameraServer.getInstance().setQuality(30);
            CameraServer.getInstance().startAutomaticCapture("cam0");
        }
        catch (Exception e)
        {
            log.error("Starting Camera Server failed with exception " + e.getMessage());
        }

        //scriptedAutonomous = new ScriptedAutonomous(); commenting this to run the autochooser
    }

    public void autonomousInit()
    {
        scriptedAutonomous.getCommandGroup().start();
        scriptedAutonomous = (ScriptedAutonomous) autoChooser.getSelected();
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
        if(scriptedAutonomous.getCommandGroup() != null)
        {
            scriptedAutonomous.getCommandGroup().cancel();
        }
    }

    public void disabledPeriodic()
    {
        if(OI.getInstance().mainGamepad.getRawButton(SHARPGamepad.BUTTON_START))
        {
            scriptedAutonomous.load();
        }
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
