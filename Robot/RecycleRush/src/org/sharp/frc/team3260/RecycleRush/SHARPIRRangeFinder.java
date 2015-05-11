package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * SHARP GP2Y0A02YK0F
 * Good for distances of 20cm to 150cm (7.87402 inches to 59.0551 inches)
 */
public class SHARPIRRangeFinder extends SensorBase
{
    protected AnalogInput analog;

    public SHARPIRRangeFinder(int port)
    {
        analog = new AnalogInput(port);

        analog.setAverageBits(20);
    }

    public double getVoltage()
    {
        return analog.getAverageVoltage();
    }

    public double getDistanceCM()
    {
        double averageVoltage = getVoltage();

        return 306.439 + averageVoltage * (-512.611 + averageVoltage * (382.268 + averageVoltage * (-129.893 + averageVoltage * 16.2537)));
    }

    public double getDistanceIn()
    {
        return getDistanceCM() / 2.54;
    }
}