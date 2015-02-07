package org.sharp.frc.team3260.RecycleRush.utils;

import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public abstract class ConstantsBase
{
    private static final Log log = new Log("ConstantsBase", Log.ATTRIBUTE_TIME);

    private static final Vector constants = new Vector();
    private static final String CONSTANTS_FILE_PATH = "/Constants/Constants.txt";

    public static void readConstantsFromFile()
    {
        try
        {
            String file = Util.getFile(CONSTANTS_FILE_PATH);
            if (file.length() < 1)
            {
                throw new IOException("Not overriding Constants");
            }

            String[] lines = Util.split(file, "\n");

            for (String curLine : lines)
            {
                String[] line = Util.split(curLine, "=");
                if (line.length != 2)
                {
                    log.error("Invalid Constants file line: " + (curLine.length() == 0 ? "(Empty Line)" : curLine));

                    continue;
                }

                boolean found = false;

                for (int j = 0; j < constants.size(); j++)
                {
                    Constant constant = (Constant) constants.elementAt(j);

                    if (constant.getName().compareTo(line[0]) == 0)
                    {
                        System.out.println("ConstantsBase - Setting " + constant.getName() + " to " + Double.parseDouble(line[1]));
                        constant.setVal(Double.parseDouble(line[1]));
                        found = true;
                        break;
                    }
                }

                if (!found)
                {
                    log.error("Constants doesn't exist " + curLine);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            log.error("Unable to open Constants.txt");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String generateHtml()
    {
        String str = "<html><head><title>Chezy Constants</title></head><body>" + "<form method=\"post\">";
        for (int i = 0; i < constants.size(); ++i)
        {
            str += ((Constant) constants.elementAt(i)).toHtml();
        }
        str += "<input type=\"submit\" value=\"Submit\">";
        str += "</form>";
        str += "</body></html>";
        return str;
    }

    public static void writeConstant(String name, double value)
    {
        Constant constant;
        for (int i = 0; i < constants.size(); i++)
        {
            constant = ((Constant) constants.elementAt(i));
            if (constant.name.equals(name))
            {
                constant.setVal(value);
            }
        }
    }

    public static class Constant
    {
        private String name;
        private double value;

        public Constant(String name, double value)
        {
            this.name = name;
            this.value = value;
            constants.addElement(this);
        }

        public String getName()
        {
            return name;
        }

        public double getDouble()
        {
            return value;
        }

        public int getInt()
        {
            return (int) value;
        }

        public void setVal(double value)
        {
            this.value = value;
        }

        public String toHtml()
        {
            String str = "<html>" + this.name + ": " + "<input type='text' value=\"" + this.value + "\" name=\"" + this.name + "\"> <br/>";

            return str;
        }

        public String toString()
        {
            return name + ": " + value;
        }
    }
}