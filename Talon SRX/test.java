package org.usfirst.frc.team3260.robot;

import org.apache.commons.csv.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


public class test {
	
	public static void main(String [ ] args) throws IOException
	{
        {
            File file = new File("C:\\Users\\NCS Customer\\Desktop\\csv.csv"); //need to make sure if this is the correct path

            FileReader fileReader = new FileReader(file);

            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            List<String> headers = Arrays.asList(line.split(","));

            Map<String, List<String>> mappedByHeader = new HashMap<String, List<String>>();

            CSVParser csvFileParser;
            CSVFormat csvFileFormat;
            List<CSVRecord> csvRecords;
            String desiredValue;
            ArrayList<String> values = new ArrayList<String>();

            // run through each key add its values to the vector
 
            for (int i = 0; i < headers.size(); i++)
            {
                String currentHeader = headers.get(i);
                System.out.println(currentHeader);
                  
                csvFileFormat = CSVFormat.DEFAULT.withHeader();
                
                csvFileParser = new CSVParser(fileReader, csvFileFormat);
                
                csvRecords = (List) csvFileParser.getRecords();
                System.out.println();
                
                for (int k =0; k < csvRecords.size(); k++)
                {
                    desiredValue = csvRecords.get(k).get(currentHeader);
                    values.add(desiredValue);
                    System.out.println(desiredValue);
                }
                
                mappedByHeader.put(currentHeader, values);
                //System.out.println(mappedByHeader.get(currentHeader).get(0));
               
            }
            
            for (int i = 0; i < mappedByHeader.get("ID").size(); i++)
            {
            	
                /* Add the ID's and process their given variables. */
                int currentID = Integer.parseInt(mappedByHeader.get("ID").get(i));
                System.out.println(currentID);
                double distance, speed, time;
                int elevatorPosition;
                
                

                distance = Double.parseDouble(mappedByHeader.get("Drive Distance").get(i));
                speed = Double.parseDouble(mappedByHeader.get("Drive Speed").get(i));
                time = Double.parseDouble(mappedByHeader.get("Time Out").get(i));
                elevatorPosition = Integer.parseInt(mappedByHeader.get("Elevator Position").get(i));
	}
	
	
	

}
	}
}

