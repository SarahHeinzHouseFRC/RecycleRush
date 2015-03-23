package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.autonomous.DefaultAutonomousCommandGroup;
import org.sharp.frc.team3260.RecycleRush.autonomous.ScriptedAutonomous;
import org.sharp.frc.team3260.RecycleRush.autonomous.ThreeToteAutonomous;
import org.sharp.frc.team3260.RecycleRush.commands.FieldCentricMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.ZeroGyroCommand;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.Arms;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Lights;
import org.sharp.frc.team3260.RecycleRush.utils.Util;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

import java.io.File;
import java.io.FileWriter;

public class Robot extends IterativeRobot
{
    private static final Log log = new Log("RobotBase", Log.ATTRIBUTE_TIME);

    private static Robot instance;

    private ScriptedAutonomous scriptedAutonomous;
    private SendableChooser autoChooser;

    protected static boolean doStatusUpdate = true;

    public Robot()
    {
        if(instance != null)
        {
            DriverStation.reportError("Error: Attempted to create a second instance of Robot class.", true);
        }

        instance = this;
    }

    public void robotInit()
    {
        log.info("Creating subsystem instances...");
        new DriveTrain();
        new Elevator();
        new Arms();
        new Lights();

        log.info("Adding SmartDashboard buttons...");
        SmartDashboard.putData("SHARPDrive", new SHARPDriveCommand());
        SmartDashboard.putData("Field Centric Mecanum Drive", new FieldCentricMecanumDriveCommand());
        SmartDashboard.putData("SHARP Mecanum Drive", new SHARPMecanumDriveCommand());
        SmartDashboard.putData("Zero Gyro", new ZeroGyroCommand());

        log.info("Indexing Autonomous Options...");
        loadAutonomousChooser();

        boolean hasCamera = true;

//        log.info("Attempting to start Camera Server...");
//        try
//        {
//            CameraServer.getInstance().setQuality(15);
//            CameraServer.getInstance().setSize(1);
//            CameraServer.getInstance().startAutomaticCapture("cam0");
//        }
//        catch(Exception e)
//        {
//            log.error("Starting Camera Server failed with exception " + e.getMessage());
//
//            hasCamera = false;
//        }
//
//        if(!hasCamera)
//        {
//            log.info("Attempting to start Camera Server with cam1...");
//            try
//            {
//                CameraServer.getInstance().setQuality(15);
//                CameraServer.getInstance().setSize(1);
//                CameraServer.getInstance().startAutomaticCapture("cam1");
//            }
//            catch(Exception e)
//            {
//                log.error("Starting Camera Server failed with exception " + e.getMessage());
//            }
//        }

        log.info("Creating instance of ScriptedAutonomous...");
        scriptedAutonomous = new ScriptedAutonomous();

        log.info("Attempting to load Elevator state from previous run...");
        try
        {
            String elevatorPositionString = Util.getFile("//U//Elevator Position.txt").replace(" ", "").replace("\n", "".replace("\r", ""));

            System.out.println(elevatorPositionString);

            int elevatorPosition = Integer.parseInt(elevatorPositionString);

            if(elevatorPosition > Elevator.ElevatorPosition.GROUND.encoderValue && elevatorPosition < Elevator.ElevatorPosition.TOP.encoderValue)
            {
                log.info("Elevator position set to " + elevatorPosition + ".");

                Elevator.getInstance().setElevatorPosition(elevatorPosition);
            }

            if(Elevator.getInstance().getTalon().isRevLimitSwitchClosed())
            {
                log.info("Limit switch currently held, setting Elevator position to 0.");

                Elevator.getInstance().setElevatorPosition(0);
            }
        }
        catch(Exception e)
        {
            log.error("Failed to load Elevator state, exception: " + e.toString());
        }

        log.info("Deleting old log files...");
        Log.deleteOldLogFiles();

        log.info("Creating status updater...");
        new Thread(new StatusUpdater()).start();
    }

    public void autonomousInit()
    {
        scriptedAutonomous.getCommandGroup().start();
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

    public void disabledInit()
    {
        if(scriptedAutonomous.getCommandGroup() != null)
        {
            scriptedAutonomous.getCommandGroup().cancel();
        }
    }

    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();

        scriptedAutonomous.setPathToJSON(autoChooser.getSelected().toString());

        if(OI.getInstance().mainGamepad.getRawButton(SHARPGamepad.BUTTON_START))
        {
            loadAutonomousChooser();

            scriptedAutonomous.load();
        }
//
//        if(OI.getInstance().mainGamepad.getRawButton(SHARPGamepad.BUTTON_SELECT))
//        {
//            DriveTrain.getInstance().zeroGyro();
//
//            DriveTrain.getInstance().zeroEncoders();
//        }
    }

    private void loadAutonomousChooser()
    {
        autoChooser = new SendableChooser();
        autoChooser.addDefault(BasicAutonomousCommandGroup.class.getSimpleName(), BasicAutonomousCommandGroup.class.getSimpleName());

        try
        {
            String autonDirectory = "/U/Autonomous/";

            File[] listOfAutoFiles = new File(autonDirectory).listFiles();

            if(listOfAutoFiles == null)
            {
                log.severe("Autonomous chooser directory does not exist");

                return;
            }

            log.info("Loading autonomous options, found " + listOfAutoFiles.length + " files in " + autonDirectory + ".");

            if(listOfAutoFiles.length != 0)
            {
                for(File autoOption : listOfAutoFiles)
                {
                    if(autoOption.getName().toLowerCase().contains("json"))
                    {
                        log.info("Added Autonomous Option " + autoOption.getName() + ".");
                        autoChooser.addObject(autoOption.getName(), autoOption.getName());
                    }
                }
            }
            else
            {
                log.info("Found Zero Autonomous Options in " + autonDirectory + ".");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    private class StatusUpdater implements Runnable
    {
        @Override
        public void run()
        {
            while(doStatusUpdate)
            {
                SmartDashboard.putNumber("Gyro Yaw", DriveTrain.getInstance().getIMU().getYaw());

                double pressure = DriveTrain.getInstance().getPressure();

                SmartDashboard.putNumber("Pressure", pressure);

                Arms.getInstance().postRangeFinderValues();

                Lights.getInstance().updateLights();

                Timer.delay(0.1);
            }
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
