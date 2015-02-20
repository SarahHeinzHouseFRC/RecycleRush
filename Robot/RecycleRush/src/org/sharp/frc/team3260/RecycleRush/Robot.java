package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.autonomous.BasicAutonomousCommandGroup;
import org.sharp.frc.team3260.RecycleRush.autonomous.ScriptedAutonomous;
import org.sharp.frc.team3260.RecycleRush.commands.FIRSTMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.FieldCentricMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.ZeroGyroCommand;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;
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

    private boolean showedPressureWarning, showedBatteryWarning;

    public Robot()
    {
        if(instance == null)
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
        showedBatteryWarning = false;
        showedPressureWarning = false;

        log.info("Creating subsystem instances...");
        new DriveTrain();
        new Elevator();
        new Gripper();

        log.info("Adding SmartDashboard buttons...");
        SmartDashboard.putData("SHARPDrive", new SHARPDriveCommand());
        SmartDashboard.putData("Field Centric Mecanum Drive", new FieldCentricMecanumDriveCommand());
        SmartDashboard.putData("FIRST Mecanum Drive", new FIRSTMecanumDriveCommand());
        SmartDashboard.putData("Zero Gyro", new ZeroGyroCommand());

        log.info("Indexing Autonomous Options...");
        loadAutonomousChooser();

        log.info("Attempting to start Camera Server...");
        try
        {
            CameraServer.getInstance().setQuality(30);
            CameraServer.getInstance().startAutomaticCapture("cam0");
        }
        catch(Exception e)
        {
            log.error("Starting Camera Server failed with exception " + e.getMessage());
        }

        log.info("Creating instance of ScriptedAutonomous...");
        scriptedAutonomous = new ScriptedAutonomous();

        log.info("Attempting to load Elevator state from previous run...");
        try
        {
            String elevatorPositionString = Util.getFile("//U//Elevator Position.txt");

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
        Runnable statusUpdater = Robot.getInstance()::updateStatus;
        statusUpdater.run();
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

        int elevatorPosition = Elevator.getInstance().getPosition();

        log.info("Attempting to save Elevator position of " + elevatorPosition + " to flash drive");

        if(elevatorPosition < 0)
        {
            log.warn("Not saving Elevator position, value less than zero.");
        }

        elevatorPosition = 0;

        try
        {
            File elevatorPositionFile = new File("//U//Elevator Position.txt");

            elevatorPositionFile.delete();
        }
        catch(Exception e)
        {
            log.warn("Deleting /U/Elevator Position.txt failed.");
        }

        try
        {
            File elevatorPositionFile = new File("//U//Elevator Position.txt");

            FileWriter fileWriter = new FileWriter(elevatorPositionFile, false);
            fileWriter.write(elevatorPosition);
            fileWriter.close();
        }
        catch(Exception e)
        {
            log.error("Saving /media/sda1/Elevator Position.txt failed, exception: " + e.toString());
        }
    }

    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();

        scriptedAutonomous.setPathToCSV(autoChooser.getSelected().toString());

        if(OI.getInstance().mainGamepad.getRawButton(SHARPGamepad.BUTTON_START))
        {
            loadAutonomousChooser();

            scriptedAutonomous.load();
        }
    }

    private void loadAutonomousChooser()
    {
        autoChooser = new SendableChooser();
        autoChooser.addDefault(BasicAutonomousCommandGroup.class.getName(), BasicAutonomousCommandGroup.class.getName());
        try
        {
            String autonDirectory = "/U/Autonomous/";

            File[] listOfAutoFiles = new File(autonDirectory).listFiles();

            log.info("Loading autonomous options, found " + listOfAutoFiles.length + " files in " + autonDirectory + ".");

            if(listOfAutoFiles.length != 0)
            {
                for(File autoOption : listOfAutoFiles)
                {
                    log.info("Added Autonomous Option " + autoOption.getName() + ".");
                    autoChooser.addObject(autoOption.getName(), autoOption.getName());
                }
            }
            else
            {
                log.info("Found zero Autonomous Options in " + autonDirectory + ".");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public void updateStatus()
    {
        SmartDashboard.putNumber("Gyro Yaw", DriveTrain.getInstance().getIMU().getYaw());

        double batteryVoltage = DriverStation.getInstance().getBatteryVoltage();
        double pressure = DriveTrain.getInstance().getPressure();

        SmartDashboard.putNumber("Pressure", pressure);

        if(pressure < 40)
        {
            if(!showedPressureWarning)
            {
                log.warn("Pneumatics pressure severely low. Current pressure: " + pressure + " PSI.");
            }

            showedPressureWarning = true;
        }
        else
        {
            showedPressureWarning = false;
        }

        if(batteryVoltage < 10)
        {
            if(!showedBatteryWarning)
            {
                log.warn("Battery Voltage severely low. Current voltage: " + batteryVoltage + " Volts.");
            }

            showedBatteryWarning = true;
        }
        else
        {
            showedBatteryWarning = false;
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
