package org.sharp.frc.team3260.RecycleRush.utils.logs;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Log
{
    public static final int ATTRIBUTE_TIME = 1;
    public static final int ATTRIBUTE_THREAD = 2;
    public static final LogLevel INFO = new LogLevel("INFO");//.setPrintStream(DriverStationLog.getInstance());
    public static final LogLevel WARN = new LogLevel("WARN");//.setPrintStream(DriverStationLog.getInstance());
    public static final LogLevel ERROR = new LogLevel("ERROR");//.setPrintStream(DriverStationLog.getInstance());
    public static final LogLevel SEVERE = new LogLevel("SEVERE");//.setPrintStream(DriverStationLog.getInstance());
    private static final int ATTRIBUTE_DEFAULT = ATTRIBUTE_TIME | ATTRIBUTE_THREAD;
    public static ArrayList<String> backlog = new ArrayList<>();
    public DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

    protected int attr;
    protected String name;

    public Log(String name, int attributes)
    {
        this.attr = attributes;
        this.name = name;
    }

    public Log(String name)
    {
        this.attr = ATTRIBUTE_DEFAULT;
        this.name = name;
    }

    String getTime()
    {
        return dateFormat.format(new Date());
    }

    private void log(String message, String level, PrintStream ps)
    {
        StringBuilder builder = new StringBuilder();

        if ((attr & ATTRIBUTE_TIME) == ATTRIBUTE_TIME)
        {
            builder.append("[" + getTime() + "] ");
        }

        builder.append("[" + name + "] ");

        if ((attr & ATTRIBUTE_THREAD) == ATTRIBUTE_THREAD)
        {
            builder.append("[" + Thread.currentThread().getName() + "] ");
        }

        builder.append("[" + level + "] ");
        builder.append(message);

        String ts = builder.toString();

        ps.println(ts);

        backlog.add(ts);
    }

    public void log(String message, LogLevel level)
    {
        log(message, level.getName().toUpperCase(), level.getPrintSteam());
    }

    public void info(String message)
    {
        log(message, INFO);
    }

    public void warn(String message)
    {
        log(message, WARN);
    }

    public void error(String message)
    {
        log(message, ERROR);
    }

    public void severe(String message)
    {
        log(message, SEVERE);
    }
}
