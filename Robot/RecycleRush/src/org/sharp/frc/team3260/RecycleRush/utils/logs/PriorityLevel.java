package org.sharp.frc.team3260.RecycleRush.utils.logs;

import java.io.PrintStream;

public class PriorityLevel
{
    String name;
    PrintStream stream = System.out;

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

    public PriorityLevel setPrintStream(PrintStream stream)
    {
        this.stream = stream;
        return this;
    }

    public PrintStream getPrintSteam()
    {
        return stream;
    }
}
