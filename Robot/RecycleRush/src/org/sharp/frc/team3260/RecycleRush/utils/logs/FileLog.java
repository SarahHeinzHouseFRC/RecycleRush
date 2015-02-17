package org.sharp.frc.team3260.RecycleRush.utils.logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class FileLog extends PrintStream
{
    protected static HashMap<String, FileLog> instances = new HashMap<>();

    private FileLog(File file) throws FileNotFoundException
    {
        super(new FileOutputStream(file));
    }

    public static FileLog getInstance(File file) throws FileNotFoundException
    {
        String fileName = file.getName();

        if(!instances.containsKey(fileName))
        {
            instances.put(fileName, new FileLog(file));
        }

        return instances.get(fileName);
    }
}
