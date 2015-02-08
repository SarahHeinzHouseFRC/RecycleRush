package org.sharp.frc.team3260.RecycleRush.Autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import edu.wpi.first.wpilibj.command.Scheduler;

import org.apache.commons.csv.*;
import org.sharp.frc.team3260.RecycleRush.commands.DriveDistanceCommand;

/**
 * Created by NCS Customer on 2/8/2015.
 */
public class ScriptedAutonomous extends CommandGroup {

    public ScriptedAutonomous() throws  IOException {

        File file = new File("C:\\Users\\NCS Customer\\Desktop\\csv.csv");
        FileReader fileReader = new FileReader(file);

        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        ArrayList<String> headers = (ArrayList<String>) Arrays.asList(line.split(","));

        Map<String, ArrayList<String>> mappedByHeader = new HashMap<String, ArrayList<String> >();

        CSVParser csvFileParser;
        CSVFormat csvFileFormat;
        List<CSVRecord> csvRecords;
        String desiredValue;


        // run through each key add its values to the vector
        for (int i = 0; i < headers.size(); i++) {
            try {
                String currentHeader = headers.get(i);
                csvFileFormat = CSVFormat.DEFAULT.withHeader(currentHeader);
                csvFileParser = new CSVParser(fileReader, csvFileFormat);
                csvRecords =(List)csvFileParser.getRecords();
                for(CSVRecord record : csvRecords) {
                    desiredValue = record.get(currentHeader);
                    mappedByHeader.get(currentHeader).add(desiredValue);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < mappedByHeader.get("ID").size(); i++){

             /* TODO: Add the rest of the ID's and process their given variables. */

            int currentID = Integer.parseInt(mappedByHeader.get("ID").get(i));

            double distance, speed, time;

            distance = Double.parseDouble(mappedByHeader.get("Drive Distance").get(i));
            speed = Double.parseDouble(mappedByHeader.get("Drive Speed").get(i));


            switch (currentID){
                //drive forward
                case 1:

                    Scheduler.getInstance().add(new DriveDistanceCommand(distance, speed));
                    break;

                //drive backward
                case -1:
                    Scheduler.getInstance().add(new DriveDistanceCommand(distance, speed*-1));

                    break;

                //strafe right
                case 2:


                    break;

                //strafe left
                case -2:



                    break;

            }





        }

    }



}


