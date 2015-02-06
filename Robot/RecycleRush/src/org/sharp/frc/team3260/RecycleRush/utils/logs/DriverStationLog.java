package org.sharp.frc.team3260.RecycleRush.utils.logs;

import edu.wpi.first.wpilibj.DriverStation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class DriverStationLog extends PrintStream
{
    protected static DriverStationLog instance;

    public DriverStationLog()
    {
        super(new OutputStream()
        {
            public StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void write(int b) throws IOException
            {
                stringBuilder.append((char) b);
            }

            @Override
            public void flush()
            {
                DriverStation.reportError(stringBuilder.toString(), false);
                
                stringBuilder = new StringBuilder();
            }
        });
    }

    public static DriverStationLog getInstance()
    {
        if(instance == null)
        {
            instance = new DriverStationLog();
        }

        return instance;
    }
}
