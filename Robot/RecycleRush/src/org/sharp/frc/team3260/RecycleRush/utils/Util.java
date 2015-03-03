package org.sharp.frc.team3260.RecycleRush.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Util
{
    public static String getFile(String fileName)
    {
        String content = "";

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName)))
        {
            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();

            while(line != null)
            {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }

            content = stringBuilder.toString();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * Returns the array of substrings obtained by dividing the given input string at each occurrence
     * of the given delimiter.
     */
    public static String[] split(String input, String delimiter)
    {
        Vector<String> nodes = new Vector<>();
        int index = input.indexOf(delimiter);
        while(index >= 0)
        {
            nodes.addElement(input.substring(0, index));
            input = input.substring(index + delimiter.length());
            index = input.indexOf(delimiter);
        }
        nodes.addElement(input);

        String[] retString = new String[nodes.size()];
        for(int i = 0; i < nodes.size(); ++i)
        {
            retString[i] = nodes.elementAt(i);
        }

        return retString;
    }

    /**
     * Limits a value to the -1.0 to +1.0 range.
     *
     * @param num the number to limit
     * @return the limited number
     */
    public static double limit(double num)
    {
        if(num > 1.0)
        {
            return 1.0;
        }
        else if(num < -1.0)
        {
            return -1.0;
        }

        return num;
    }

    /**
     * Rotate a vector in Cartesian space.
     *
     * @param x     the x component of the vector
     * @param y     the y component of the vector
     * @param angle the angle to rotate the vector
     * @return a 2 element array containing the rotated vector
     */
    public static double[] rotateVector(double x, double y, double angle)
    {
        double cosA = Math.cos(angle * (Math.PI / 180.0));
        double sinA = Math.sin(angle * (Math.PI / 180.0));
        double out[] = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        return out;
    }

    public static double limit(double val, double limit)
    {
        return (Math.abs(val) < Math.abs(limit)) ? val : (val < 0 ? -limit : limit);
    }

    public static double handleDeadband(double value, double deadband)
    {
        return (Math.abs(value) > Math.abs(deadband)) ? value : 0.0;
    }
}