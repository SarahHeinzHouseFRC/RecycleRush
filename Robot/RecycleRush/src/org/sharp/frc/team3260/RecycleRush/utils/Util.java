package org.sharp.frc.team3260.RecycleRush.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/*
 * Utilities that we stole from a bunch of random teams
 * #YOLO
 */
public class Util
{
    // Prevent this class from being instantiated.
    private Util()
    {
    }

    public static String getFile(String fileName)
    {
        String content = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            StringBuilder sb = new StringBuilder();

            String line = br.readLine();

            while (line != null)
            {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            content = sb.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Filename:" + fileName + "\t#:" + content.length());
        return content;
    }

    /**
     * Returns the array of substrings obtained by dividing the given input string at each occurrence
     * of the given delimiter.
     */
    public static String[] split(String input, String delimiter)
    {
        Vector node = new Vector();
        int index = input.indexOf(delimiter);
        while (index >= 0)
        {
            node.addElement(input.substring(0, index));
            input = input.substring(index + delimiter.length());
            index = input.indexOf(delimiter);
        }
        node.addElement(input);

        String[] retString = new String[node.size()];
        for (int i = 0; i < node.size(); ++i)
        {
            retString[i] = (String) node.elementAt(i);
        }

        return retString;
    }

    public static boolean isNumber(String str)
    {
        try
        {
            double d = Double.parseDouble(str);

            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static String toJson(Hashtable data)
    {
        return toJson(data, 0);
    }

    public static String toJson(Hashtable data, int levels)
    {
        String res = "{\n";

        for (Enumeration en = data.keys(); en.hasMoreElements(); )
        {
            String key = (String) en.nextElement();
            res += "\t\"" + key + "\": ";

            try
            {
                Hashtable nestedData = (Hashtable) data.get(key);
                for (int i = 0; i < levels; i++)
                {
                    res += "\t";
                }
                res += toJson(nestedData, levels + 2);
            }
            catch (ClassCastException e)
            {
                String value = data.get(key).toString();
                if (Util.isNumber(value))
                {
                    res += value;
                }
                else
                {
                    res += "\"" + value + "\"";
                }
            }

            res += en.hasMoreElements() ? ",\n" : "\n";
        }
        res += "}";
        return res;
    }

    /**
     * Limits a value to the -1.0 to +1.0 range.
     *
     * @param num the number to limit
     * @return the limited number
     */
    public static double limit(double num)
    {
        if (num > 1.0)
        {
            return 1.0;
        }
        else if (num < -1.0)
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

    /**
     * Normalize all wheel speeds if the magnitude of any wheel is greater than
     * 1.0.
     *
     * @param wheelSpeeds the array of wheel speeds to normalize
     */
    public static void normalize(double wheelSpeeds[])
    {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);

        // Loops through each number to find the biggest absolute value.
        for (int i = 1; i < wheelSpeeds.length; i++)
        {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp)
            {
                maxMagnitude = temp;
            }
        }

        // If the maximum is greater than 1.0, reduce all the values down
        // proportionally so the maximum becomes 1.0.
        if (maxMagnitude > 1.0)
        {
            for (int i = 0; i < wheelSpeeds.length; i++)
            {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
    }

    public static double limit(double val, double limit)
    {
        return (Math.abs(val) < Math.abs(limit)) ? val : (val < 0 ? -limit : limit);
    }

    /**
     * Returns the arithmetic mean of the array.
     *
     * @param vals values to find average of
     * @return the mean value
     */
    public static double mean(double[] vals)
    {
        double sum = 0;

        for (int x = 0; x < vals.length; x++)
        {
            sum += vals[x];
        }

        return sum / (double) vals.length;
    }
}