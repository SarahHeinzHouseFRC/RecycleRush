package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.lang.Integer;
import java.util.*;
import java.util.HashMap;
import java.util.Iterator;

public class ScriptedAutonomous
{

    private static final Log log = new Log("ScriptedAutonomous", Log.ATTRIBUTE_TIME);



    private CommandGroup commandGroup;

    private boolean commandsLoaded;
    private String pathToCSV;

    public ScriptedAutonomous()
    {
        load();
    }

    private boolean loadCSV()
    {
            JSONParser parser = new JSONParser();
            Object obj = new Object();

        try {
            obj = parser.parse(new FileReader("//home//lvuser//defaultAutonomous.json"));
        }
        catch (org.json.simple.parser.ParseException | IOException e)
        {
            log.error("Json File not Found");
        }

            JSONObject jsonObject = (JSONObject) obj;

            String autonomousName = (String) jsonObject.get("Autonomous Name");
            JSONArray commandList = (JSONArray) jsonObject.get("Commands");

            boolean classFound = false;
            Iterator iterator = commandList.iterator();
            while(iterator.hasNext())
            {
                JSONObject currentCommand = (JSONObject) iterator.next();
                String robotClass = (String) currentCommand.get("Robot Class");
                Class commandClass = null;
                try{
                     commandClass = Class.forName(robotClass);
                    classFound = true;
                }catch (ClassNotFoundException e) {
                    log.info("Class: " + robotClass + " not found");
                }
                if(classFound){
                    switch (robotClass)
                    {
                        case "DriveDistanceCommand":
                            int distance = Integer.parseInt(currentCommand.get("Disance").toString());

                            addSequential(new DriveDistanceCommand(distance));
                            break;

                        case "DriveAtSpeedCommand":
                            int time = Integer.parseInt(currentCommand.get("Time").toString());
                            int speed = Integer.parseInt(currentCommand.get("Speed").toString());

                            addSequential(new DriveAtSpeedCommand(speed,time));
                            break;

                        case "RotateToHeadingCommand":
                            int angle = Integer.parseInt(currentCommand.get("Angle to Rotate").toString());

                            addSequential(new RotateToHeadingCommand(angle,true));
                            break;

                        case "OpenGripperCommand":
                            addSequential(new OpenGripperCommand());
                            break;

                        case "CloseGripperCommand":
                            addSequential(new CloseGripperCommand);
                            break;

                        case "RobotIdleCommand":
                            int time = Integer.parseInt(currentCommand.get("Time").toString());
                            addSequential(new RobotIdleCommand(time));
                            break;

                        case "ElevatorToSetpointCommand":
                            int elevatorSetPoint = Integer.parseInt(currentCommand.get("Elevator Setpoint").toString());

                            addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositonByIndex(elevatorSetPoint)));
                            break;

                        case "ZeroGyroCommand":
                            addSequential(new ZeroGyroCommand());
                            break;
                    }
                }
            }

        int numCommandsAdded = 0;

        try
        {
            File file;

            if(pathToCSV == null)
            {
                file = new File("//home//lvuser//defaultAutonomous.json");
            }
            else
            {
                file = new File(pathToCSV);

                if(!file.exists())
                {
                    file = new File("//home//lvuser//defaultAutonomous.json");
                }
            }


        }
        catch(Exception ex)
        {
            ex.printStackTrace();

            return false;
        }

        try
        {
            File file;

            if(pathToCSV == null)
            {
                file = new File("//home//lvuser//defaultAutonomous.csv");
            }
            else
            {
                file = new File(pathToCSV);

                if(!file.exists())
                {
                    file = new File("//home//lvuser//defaultAutonomous.csv");
                }
            }

            FileReader fileReader = new FileReader(file);

            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            List<String> headers = Arrays.asList(line.split(","));

            Map<String, List<String>> mappedByHeader = new HashMap<>();

            CSVParser csvFileParser;
            CSVFormat csvFileFormat;

            String[] headerArray = new String[headers.size()];
            headerArray = headers.toArray(headerArray);

            List<CSVRecord> csvRecords;

            // run through each key add its values to the vector

            csvFileFormat = CSVFormat.DEFAULT.withHeader(headerArray).withSkipHeaderRecord();

            csvFileParser = new CSVParser(new FileReader(file), csvFileFormat);

            csvRecords = csvFileParser.getRecords();

            if(csvRecords != null)
            {
                if(!csvRecords.isEmpty())
                {
                    for(int i = 0; i < csvRecords.size(); i++)
                    {
                        for(String header : headers)
                        {
                            if(i == 0)
                            {
                                mappedByHeader.put(header, new ArrayList<>());
                            }

                            mappedByHeader.get(header).add(csvRecords.get(i).get(header));
                        }
                    }

                    for(int i = 0; i < mappedByHeader.get("ID").size(); i++)
                    {
                        /* Add the ID's and process their given variables. */
                        int currentID = Integer.parseInt(mappedByHeader.get("ID").get(i));
                        double distance, time, timeout;
                        int elevatorPosition, degreeToRotate;
                        boolean zeroGyro;

                        distance = Double.parseDouble(mappedByHeader.get("Drive Distance").get(i));
                        time = Double.parseDouble(mappedByHeader.get("Time Out").get(i));
                        elevatorPosition = Integer.parseInt(mappedByHeader.get("Elevator Position").get(i));
                        degreeToRotate = Integer.parseInt(mappedByHeader.get("Degree to Rotate").get(i));
                        zeroGyro = Integer.parseInt(mappedByHeader.get("Zero Gyro").get(i)) != 0;
                        timeout = Double.parseDouble(mappedByHeader.get("Command Timeout").get(i));

                        switch(currentID)
                        {
                            //drive forward
                            case 1:
                                //18.85 inches per rotation
                                //163 tick per ft
                                //0.07 inches per tick
                                numCommandsAdded++;
                                if(timeout != 0)
                                {
                                    addSequential(new DriveDistanceCommand(distance, timeout / 1000));
                                }
                                else
                                {
                                    addSequential(new DriveDistanceCommand(distance));
                                }
                                break;

                            //drive backward
                            case -1:
                                numCommandsAdded++;
                                if(timeout != 0)
                                {
                                    addSequential(new DriveDistanceCommand(-distance, timeout / 1000));
                                }
                                else
                                {
                                    addSequential(new DriveDistanceCommand(-distance));
                                }
                                break;

                            //rotate right
                            case 2:
                                numCommandsAdded++;
                                addSequential(new RotateToHeadingCommand((double) -degreeToRotate, true));
                                if(zeroGyro)
                                {
                                    addSequential(new ZeroGyroCommand());
                                }
                                break;

                            //rotate left
                            case -2:
                                numCommandsAdded++;
                                addSequential(new RotateToHeadingCommand((double) degreeToRotate, true));
                                if(zeroGyro)
                                {
                                    addSequential(new ZeroGyroCommand());
                                }
                                break;

                            case 5:
                                numCommandsAdded++;
                                addSequential(new RobotIdleCommand(time));
                                break;

                            //open tote
                            case 6:
                                numCommandsAdded++;
                                addSequential(new OpenGripperCommand());
                                break;

                            //close gripper
                            case -6:
                                numCommandsAdded++;
                                addSequential(new CloseGripperCommand());
                                break;

                            //elevator up- should be set to point?
                            case 7:
                                numCommandsAdded++;
                                addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(elevatorPosition)));
                                break;

                            //elevator down - should be set to point?
                            case -7:
                                numCommandsAdded++;
                                addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(elevatorPosition)));
                                break;

                            case 8:
                                numCommandsAdded++;
                                addSequential(new ZeroGyroCommand());
                                break;
                        }
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();

            return false;
        }

        getLog().info("Added " + numCommandsAdded + " Commands.");

        return true;
    }

    public String getPathToCSV()
    {
        return pathToCSV;
    }

    public void setPathToCSV(String path)
    {
        if(!pathToCSV.equals(path))
        {
            pathToCSV = path;

            load();
        }
    }

    public void setPathToCSV(String path, boolean forced)
    {
        if(!pathToCSV.equals(path) || forced)
        {
            pathToCSV = path;

            load();
        }
    }

    public void load()
    {
        if(pathToCSV == null)
        {
            pathToCSV = BasicAutonomousCommandGroup.class.getName();
        }

        if(pathToCSV.equals(BasicAutonomousCommandGroup.class.getName()))
        {
            commandGroup = new BasicAutonomousCommandGroup();

            commandsLoaded = true;
        }
        else
        {
            commandGroup = new CommandGroup();

            commandsLoaded = loadCSV();
        }

        getLog().info("Autonomous loading was " + (commandsLoaded ? "successful." : "not successful."));

        if(!commandsLoaded)
        {
            commandGroup = new BasicAutonomousCommandGroup();
        }
    }

    private void addSequential(Command command)
    {
        commandGroup.addSequential(command);
    }

    private void addSequential(Command command, double timeout)
    {
        commandGroup.addSequential(command, timeout);
    }

    public CommandGroup getCommandGroup()
    {
        log.info("Retrieving Autonomous Command Group." + (loadedSuccessfully() ? "" : " Loading failed, Basic Auto Command Group substituted."));

        return commandGroup;
    }

    public boolean loadedSuccessfully()
    {
        return commandsLoaded;
    }

    public static Log getLog()
    {
        return log;
    }
}