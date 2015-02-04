package org.sharp.frc.team3260.RecycleRush.utils;

import java.io.IOException;
import java.util.Vector;

public abstract class ConstantsBase
{
    private static final Vector constants = new Vector();
    private static final String CONSTANTS_FILE_PATH = "Constants.txt";

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
            for (int i = 0; i < lines.length; i++)
            {
                String[] line = Util.split(lines[i], "=");
                if (line.length != 2)
                {
                    System.out.println("ConstantsBase - Error: Invalid Constants file line: " + (lines[i].length() == 0 ? "(empty line)" : lines[i]));

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
                    System.out.println("ConstantsBase - Error: Constant doesn't exist: " + lines[i]);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
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
            String str = "<html>" +
                         this.name + ": " + "<input type='text' value=\"" + this.value + "\" name=\"" + this.name + "\"> <br/>";

            return str;
        }

        public String toString()
        {
            return name + ": " + value;
        }
    }
}