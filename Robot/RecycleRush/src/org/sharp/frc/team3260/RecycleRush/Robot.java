package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.commands.DriveForwardCommand;
import org.sharp.frc.team3260.RecycleRush.commands.FieldCentricMecanumDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.SHARPDriveCommand;
import org.sharp.frc.team3260.RecycleRush.commands.ZeroGyroCommand;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Gripper;

public class Robot extends IterativeRobot
{
    private Command autonomousCommand;

    private SendableChooser autoChooser;

    public void robotInit()
    {
        new DriveTrain();
        new Elevator();
        new Gripper();

        autoChooser = new SendableChooser();
        autoChooser.addObject("Drive Forward", new DriveForwardCommand());
        SmartDashboard.putData("Auto Mode", autoChooser);

        SmartDashboard.putData("SHARPDrive", new SHARPDriveCommand());
        SmartDashboard.putData("Field Centric Mecanum Drive", new FieldCentricMecanumDriveCommand());
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

    private void log()
    {
        if (DriveTrain.getInstance().getIMU() != null)
        {
            SmartDashboard.putNumber("Gyro Angle", DriveTrain.getInstance().getIMU().getYaw());
        }
    }
}
