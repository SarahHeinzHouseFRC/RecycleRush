package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

        File[] listOfAutoFiles = new File("//U//autonomous//").listFiles();
        if(listOfAutoFiles != null)
        {
            for(File autoOption : listOfAutoFiles)
            {
                if(autoChooser.getSelected() == null)
                {
                    autoChooser.addDefault(autoOption.getName(), autoOption.getName());
                }
                else
                {
                    autoChooser.addObject(autoOption.getName(), autoOption.getName());
                }
            }
        }
        SmartDashboard.putData("Auto Chooser", autoChooser);

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

        scriptedAutonomous = new ScriptedAutonomous();

        log.info("Attempting to load Elevator state from previous run...");
        try
        {
            String elevatorPositionString = Util.getFile("//U//Elevator Position.txt");

            int elevatorPosition = Integer.parseInt(elevatorPositionString);

            if(elevatorPosition > Elevator.ElevatorPosition.GROUND.encoderValue && elevatorPosition < Elevator.ElevatorPosition.TOP.encoderValue)
            {
                Elevator.getInstance().setElevatorPosition(elevatorPosition);
            }

            if(Elevator.getInstance().getTalon().isRevLimitSwitchClosed())
            {
                Elevator.getInstance().setElevatorPosition(0);
            }
        }
        catch(Exception e)
        {
            log.error("Failed to load Elevator state, exception: " + e.toString());
        }
        
        log.info("Deleting old log files...");
        Log.deleteOldLogFiles();
    }

    public void autonomousInit()
    {
        scriptedAutonomous.getCommandGroup().start();
    }

    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();

        SmartDashboard.putNumber("Gyro Yaw", DriveTrain.getInstance().getIMU().getYaw());
        DriveTrain.getInstance().showPressure();
    }

    public void teleopInit()
    {
    }

    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();

        OI.getInstance().checkControls();

        SmartDashboard.putNumber("Gyro Yaw", DriveTrain.getInstance().getIMU().getYaw());
        DriveTrain.getInstance().showPressure();
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

        int elevatorPosition = Elevator.getInstance().getPosition();

        log.info("Attempting to save Elevator position of " + elevatorPosition + " to flash drive");

        if(elevatorPosition < 0)
        {
            log.warn("Not saving Elevator position, value less than zero.");
        }
        else if(elevatorPosition > Elevator.ElevatorPosition.TOP.encoderValue)
        {
            log.warn("Not saving Elevator position, value less than maximum.");
        }
        else
        {
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
    }

    public void disabledPeriodic()
    {
        if(!scriptedAutonomous.getPathToCSV().equals(autoChooser.getSelected().toString()))
        {
            scriptedAutonomous.setPathToCSV(autoChooser.getSelected().toString());
        }
        
        if(OI.getInstance().mainGamepad.getRawButton(SHARPGamepad.BUTTON_START))
        {
            scriptedAutonomous.load();
        }

        SmartDashboard.putNumber("Gyro Yaw", DriveTrain.getInstance().getIMU().getYaw());
        DriveTrain.getInstance().showPressure();
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
