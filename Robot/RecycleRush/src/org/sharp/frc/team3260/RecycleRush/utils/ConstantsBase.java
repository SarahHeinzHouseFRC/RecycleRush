package org.sharp.frc.team3260.RecycleRush.utils;

import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

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
                log.error("Constants file is blank, unable to override Constants.");
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
                        log.info("Setting " + constant.getName() + " to " + Double.parseDouble(line[1]));
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
        catch (Exception e)
        {
            log.error(e.toString());
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

        public String toString()
        {
            return name + ": " + value;
        }
    }
}