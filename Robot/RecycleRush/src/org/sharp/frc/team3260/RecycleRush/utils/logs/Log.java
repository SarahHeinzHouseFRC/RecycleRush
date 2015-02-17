package org.sharp.frc.team3260.RecycleRush.utils.logs;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log
{
    private static FileLog flashDriveLog;
    private static boolean failedToCreateFlashDriveLog = false;

    public static final int ATTRIBUTE_TIME = 1;
    public static final int ATTRIBUTE_THREAD = 2;
    public static final LogLevel INFO = new LogLevel("INFO");
    public static final LogLevel WARN = new LogLevel("WARN");
    public static final LogLevel ERROR = new LogLevel("ERROR");
    public static final LogLevel SEVERE = new LogLevel("SEVERE");
    protected static final int ATTRIBUTE_DEFAULT = ATTRIBUTE_TIME;
    private DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
    private static DateFormat fileDateFormat = new SimpleDateFormat("d MMM yyyy HH-mm-ss");

    protected int attr;
    protected String name;

    public Log(String name, int attributes)
    {
        try
        {
            createFlashDriveLog();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        this.attr = attributes;
        this.name = name;
    }

    public Log(String name)
    {
        try
        {
            createFlashDriveLog();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        this.attr = ATTRIBUTE_DEFAULT;
        this.name = name;
    }

    public static void deleteOldLogFiles()
    {
        try
        {
            long curTime = new Date().getTime();

            File logDirectory = new File("//U//Logs//");

            if(logDirectory.isDirectory())
            {
                File[] logs = logDirectory.listFiles();

                for(File curLog : logs)
                {
                    long diff = curTime - curLog.lastModified();

                    if(diff > 3 * 24 * 60 * 60 * 1000)
                    {
                        curLog.delete();
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void createFlashDriveLog()
    {
        if(!failedToCreateFlashDriveLog && flashDriveLog == null)
        {
            try
            {
                File flashDriveLogFile = new File("//U//Logs//" + getFileDate() + ".log.txt");

                if(!flashDriveLogFile.exists())
                {
                    flashDriveLogFile.createNewFile();
                }

                flashDriveLog = FileLog.getInstance(flashDriveLogFile);
            }
            catch(Exception e)
            {
                failedToCreateFlashDriveLog = true;

                e.printStackTrace();
            }
        }
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

    public void log(String message, LogLevel level)
    {
        log(message, level.getName().toUpperCase(), level.getPrintSteam());

        if(!failedToCreateFlashDriveLog)
        {
            log(message, level.getName().toLowerCase(), flashDriveLog);

            flashDriveLog.flush();
        }
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
