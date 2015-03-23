package org.sharp.frc.team3260.RecycleRush.utils.logs;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log
{
    public static final int ATTRIBUTE_TIME = 1;
    public static final int ATTRIBUTE_THREAD = 2;
    public static final PriorityLevel INFO = new PriorityLevel("INFO");
    public static final PriorityLevel WARN = new PriorityLevel("WARN");
    public static final PriorityLevel ERROR = new PriorityLevel("ERROR");
    public static final PriorityLevel SEVERE = new PriorityLevel("SEVERE");
    protected static final int ATTRIBUTE_DEFAULT = ATTRIBUTE_TIME;
    private DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
    private static DateFormat fileDateFormat = new SimpleDateFormat("d MMM yyyy HH-mm-ss");

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

    private static String getFileDate()
    {
        return fileDateFormat.format(new Date());
    }

    String getTime()
    {
        return dateFormat.format(new Date());
    }

    private void log(String message, String level, PrintStream ps)
    {
        StringBuilder builder = new StringBuilder();

        if((attr & ATTRIBUTE_TIME) == ATTRIBUTE_TIME)
        {
            builder.append("[").append(getTime()).append("] ");
        }

        builder.append("[").append(name).append("] ");

        if((attr & ATTRIBUTE_THREAD) == ATTRIBUTE_THREAD)
        {
            builder.append("[").append(Thread.currentThread().getName()).append("] ");
        }

        builder.append("[").append(level).append("] ").append(message);

        String ts = builder.toString();

        ps.println(ts);
    }

    public void log(String message, PriorityLevel level)
    {
        log(message, level.getName().toUpperCase(), System.out);
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

    public static class PriorityLevel
    {
        String name;

        public PriorityLevel(String name)
        {
            this.name = name;
        }

        public PriorityLevel setName(String n)
        {
            this.name = n;
            return this;
        }

        public String getName()
        {
            return name;
        }
    }
}
