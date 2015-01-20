package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.sharp.frc.team3260.RecycleRush.Robot;


public class Pneumatics
{
    public static Compressor compressor;

    public Pneumatics()
    {
        if (Robot.isReal())
        {
            compressor = new Compressor();
        }
    }
}