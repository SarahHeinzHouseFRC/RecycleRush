package org.sharp.frc.team3260.RecycleRush;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Honeywell PX2AN2XX150PAAAX
 */
public class SHARPPressureTransducer extends SensorBase implements LiveWindowSendable
{
    protected AnalogInput analog;

    private ITable table;

    public SHARPPressureTransducer(int port)
    {
        analog = new AnalogInput(port);

        analog.setAverageBits(12);

        System.out.println(analog.getAverageBits());

        LiveWindow.addSensor("Pressure", analog.getChannel(), this);
    }

    public double getVoltage()
    {
        return analog.getAverageVoltage();
    }

    public double getPSI()
    {
        double curPSI = 38.823 * getVoltage() - 32.976;

        if(curPSI < 0)
        {
            curPSI = 0;
        }

        return curPSI;
    }

    public void updateTable()
    {
        if(table != null)
        {
            table.putNumber("Voltage", getVoltage());
            table.putNumber("PSI", getPSI());
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
        return "Pressure";
    }
}