package org.sharp.frc.team3260.RecycleRush.utils.logs;

public class ThrottledLog extends Log
{
    private long timeBetweenPrints;

    private long lastPrintTime;

    public ThrottledLog(String name, int attributes, long timeBetweenPrints)
    {
        super(name, attributes);

        this.timeBetweenPrints = timeBetweenPrints;

        lastPrintTime = 0;
    }

    public ThrottledLog(String name, long timeBetweenPrints)
    {
        super(name);

        this.timeBetweenPrints = timeBetweenPrints;

        lastPrintTime = 0;
    }

    public void info(String message)
    {
        if (System.currentTimeMillis() - lastPrintTime > timeBetweenPrints)
        {
            log(message, INFO);

            lastPrintTime = System.currentTimeMillis();
        }
    }

    public void info(String message, boolean ignoresTime)
    {
        if (ignoresTime)
        {
            log(message, INFO);
        }
        else
        {
            info(message);
        }
    }
}
