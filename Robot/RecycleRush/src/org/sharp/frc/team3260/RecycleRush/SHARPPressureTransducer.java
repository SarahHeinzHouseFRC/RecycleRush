package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SensorBase;

/**
 * Honeywell PX2AN2XX150PAAAX
 */
public class SHARPPressureTransducer extends SensorBase
{
    protected AnalogInput analog;

    public SHARPPressureTransducer(int port)
    {
        analog = new AnalogInput(port);

        analog.setAverageBits(12);
    }

    public double getVoltage()
    {
        return analog.getAverageVoltage();
    }

    public double getPressure()
    {
        double curPSI = 38.823 * getVoltage() - 32.976;

        if(curPSI < 0)
        {
            curPSI = 0;
        }

        return curPSI;
    }
}