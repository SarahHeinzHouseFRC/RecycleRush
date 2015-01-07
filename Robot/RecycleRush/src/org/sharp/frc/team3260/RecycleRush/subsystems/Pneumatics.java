package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Robot;

/**
 * The Pneumatics subsystem contains the compressor and a pressure sensor.
 * <p/>
 * NOTE: The simulator currently doesn't support the compressor or pressure sensors.
 */
public class Pneumatics extends Subsystem
{
    AnalogInput pressureSensor;
    Compressor compressor;

    private static final double MAX_PRESSURE = 2.55;

    public Pneumatics()
    {
        pressureSensor = new AnalogInput(3);
        if (Robot.isReal())
        {
            compressor = new Compressor();
        }

        LiveWindow.addSensor("Pneumatics", "Pressure Sensor", pressureSensor);
    }

    /**
     * No default command
     */
    public void initDefaultCommand()
    {
    }

    /**
     * Start the compressor going. The compressor automatically starts and stops as it goes above and below maximum pressure.
     */
    public void start()
    {
        if (Robot.isReal())
        {
            compressor.start();
        }
    }

    /**
     * @return Whether or not the system is fully pressurized.
     */
    public boolean isPressurized()
    {
        if (Robot.isReal())
        {
            return MAX_PRESSURE <= pressureSensor.getVoltage();
        }
        else
        {
            return true; // NOTE: Simulation always has full pressure
        }
    }

    /**
     * Puts the pressure on the SmartDashboard.
     */
    public void writePressure()
    {
        SmartDashboard.putNumber("Pressure", pressureSensor.getVoltage());
    }
}
