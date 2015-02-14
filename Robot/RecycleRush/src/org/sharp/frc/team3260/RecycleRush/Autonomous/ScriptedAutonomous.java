package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ScriptedAutonomous extends CommandGroup
{
    private boolean successful;

    public ScriptedAutonomous()
    {
        try {
            File file = new File("/home/lvuser/autonomousVariables.csv"); //need to make sure if this is the correct path

            FileReader fileReader = new FileReader(file);

            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            List<String> headers = Arrays.asList(line.split(","));

            Map<String, List<String>> mappedByHeader = new HashMap<String, List<String>>();

            CSVParser csvFileParser;
            CSVFormat csvFileFormat;
            List<CSVRecord> csvRecords;
            String desiredValue;

            // run through each key add its values to the vector

                for (String currentHeader : headers) {
                    List<String> values = null;

                    csvFileFormat = CSVFormat.DEFAULT.withHeader(currentHeader);
                    csvFileParser = new CSVParser(fileReader, csvFileFormat);
                    csvRecords = (List) csvFileParser.getRecords();
                    for (CSVRecord csvRecord : csvRecords) {
                        desiredValue = csvRecord.get(currentHeader);
                        values.add(desiredValue);
                    }


                    mappedByHeader.put(currentHeader, values);
                    System.out.println(mappedByHeader.get(currentHeader));

                }
                for (int i = 0; i < mappedByHeader.get("ID").size(); i++) {
                /* Add the ID's and process their given variables. */
                    int currentID = Integer.parseInt(mappedByHeader.get("ID").get(i));

                    double distance, speed, time;
                    int elevatorPosition;

                    distance = Double.parseDouble(mappedByHeader.get("Drive Distance").get(i));
                    speed = Double.parseDouble(mappedByHeader.get("Drive Speed").get(i));
                    time = Double.parseDouble(mappedByHeader.get("Time Out").get(i));
                    elevatorPosition = Integer.parseInt(mappedByHeader.get("Elevator Position").get(i));

                    switch (currentID) {
                        //drive forward
                        case 1:

//                            addSequential(new DriveDistanceCommand(distance, speed));
                            break;

                        //drive backward
                        case -1:
//                            addSequential(new DriveDistanceCommand(distance, speed * -1));
                            break;

                        // TODO: Get rotate to work correctly
                        //rotate right
                        case 2:
                            break;

                        //rotate left
                        case -2:
                            break;

                        case 5:
                            addSequential(new IdleCommand(time));
                            break;

                        //open tote
                        case 6:
                            addSequential(new OpenGripperCommand());
                            break;

                        //close gripper
                        case -6:
                            addSequential(new CloseGripperCommand());
                            break;

                        //elevator up- should be set to point?
                        case 7:
                            addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(elevatorPosition), 10));
                            break;

                        //elevator down - should be set to point?
                        case -7:
                            addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(elevatorPosition), 10));
                            break;

                    }

                    successful = true;
                }


        }
        catch (IOException e)
        {
            e.printStackTrace();
            successful = false;
        }
    }

    public boolean commandWasSuccessFul()
    {
        return successful;
    }
}


