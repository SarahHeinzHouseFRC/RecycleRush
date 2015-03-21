package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * SHARP GP2Y0A02YK0F
 * Good for distances of 20cm to 150cm (7.87402 inches to 59.0551 inches)
 */
public class SHARPIRRangeFinder extends SensorBase implements LiveWindowSendable
{
    protected AnalogInput analog;

    private ITable table;

    public SHARPIRRangeFinder(int port)
    {
        analog = new AnalogInput(port);

        analog.setAverageBits(20);

        LiveWindow.addSensor("RangeFinder", analog.getChannel(), this);
    }

    public double getVoltage()
    {
        return analog.getAverageVoltage();
    }

    public double getDistanceCM()
    {
        return 306.439 + analog.getAverageVoltage() * (-512.611 + analog.getAverageVoltage() * (382.268 + analog.getAverageVoltage() * (-129.893 + analog.getAverageVoltage() * 16.2537)));
    }

    public double getDistanceIn()
    {
        return getDistanceCM() / 2.54;
    }

    public void updateTable()
    {
        if(table != null)
        {
            table.putNumber("Voltage", getVoltage());
            table.putNumber("Distance In", getDistanceIn());
            table.putNumber("Distanace CM", getDistanceCM());
        }
    }

    public void startLiveWindowMode()
    {
    }

    public void stopLiveWindowMode()
    {
    }

    public void initTable(ITable table)
    {
        this.table = table;

        updateTable();
    }

    public ITable getTable()
    {
        return table;
    }

    public String getSmartDashboardType()
    {
        return "RangeFinder";
    }
}