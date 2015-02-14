package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.autonomous.BasicAutoCommandGroup;
import org.sharp.frc.team3260.RecycleRush.autonomous.ScriptedAutonomous;
import org.sharp.frc.team3260.RecycleRush.commands.FIRSTMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.FieldCentricMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.ZeroGyroCommand;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

public class Robot extends IterativeRobot
{
    private ScriptedAutonomous autonomousCommandGroup;

    private static final Log log = new Log("RobotBase", Log.ATTRIBUTE_TIME);

    private static Robot instance;

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
}
