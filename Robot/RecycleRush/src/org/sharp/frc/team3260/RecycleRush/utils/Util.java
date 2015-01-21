package org.sharp.frc.team3260.RecycleRush.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Contains basic functions that are used often.
 *
 * @author richard@team254.com (Richard Lin)
 * @author brandon.gonzalez.451@gmail.com (Brandon Gonzalez)
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

		try(BufferedReader br = new BufferedReader(new FileReader(fileName)))
		{
			StringBuilder sb = new StringBuilder();

			String line = br.readLine();

			while(line != null)
			{
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}

			content = sb.toString();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
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
		while(index >= 0)
		{
			node.addElement(input.substring(0, index));
			input = input.substring(index + delimiter.length());
			index = input.indexOf(delimiter);
		}
		node.addElement(input);

		String[] retString = new String[node.size()];
		for(int i = 0; i < node.size(); ++i)
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
		catch(NumberFormatException e)
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

		for(Enumeration en = data.keys(); en.hasMoreElements(); )
		{
			String key = (String) en.nextElement();
			res += "\t\"" + key + "\": ";

			try
			{
				Hashtable nestedData = (Hashtable) data.get(key);
				for(int i = 0; i < levels; i++) res += "\t";
				res += toJson(nestedData, levels + 2);
			}
			catch(ClassCastException e)
			{
				String value = data.get(key).toString();
				if(Util.isNumber(value)) res += value;
				else res += "\"" + value + "\"";
			}

			res += en.hasMoreElements() ? ",\n" : "\n";
		}
		res += "}";
		return res;
	}
}