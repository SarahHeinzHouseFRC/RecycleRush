package org.sharp.frc.team3260.RecycleRush.utils.logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class FileLog extends PrintStream
{
    protected static HashMap<String, FileLog> instances = new HashMap<>();

    private FileLog(FileOutputStream fileOutputStream) throws FileNotFoundException
    {
        super(fileOutputStream);
    }

    public static FileLog getInstance(File file) throws FileNotFoundException
    {
        String fileName = file.getName();

        if(!instances.containsKey(fileName))
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            
            instances.put(fileName, new FileLog(fileOutputStream));        
        }

        return instances.get(fileName);
    }
}
