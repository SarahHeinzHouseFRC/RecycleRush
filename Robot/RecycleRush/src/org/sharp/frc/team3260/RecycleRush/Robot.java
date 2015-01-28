package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.commands.DriveForwardCommand;
import org.sharp.frc.team3260.RecycleRush.commands.ZeroGyroCommand;
import org.sharp.frc.team3260.RecycleRush.subsystems.Aidanvator;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;

public class Robot extends IterativeRobot
{
    private Command autonomousCommand;

    private static DriveTrain drivetrain;
    private static Aidanvator aidanvator;

    private static Gripper gripper;

    private SendableChooser autoChooser;

    public void robotInit()
    {
        drivetrain = new DriveTrain();
        gripper = new Gripper();

        autoChooser = new SendableChooser();
        autoChooser.addObject("Drive Forward", new DriveForwardCommand());
        SmartDashboard.putData("Auto Mode", autoChooser);

        SmartDashboard.putData("Zero Gyro", new ZeroGyroCommand());
    }

    public void autonomousInit()
    {
        autonomousCommand = (Command) autoChooser.getSelected();
        autonomousCommand.start();
    }

    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();
        log();
    }

    public void teleopInit()
    {
        if (autonomousCommand != null)
        {
            autonomousCommand.cancel();
        }
    }

    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();
        log();
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
        log();
    }

    public static DriveTrain getDrivetrain()
    {
        return drivetrain;
    }

    public static Aidanvator getAidanvator()
    {
        return aidanvator;
    }

    public static Gripper getGripper()
    {
        return gripper;
    }

    private void log()
    {
    }
}
