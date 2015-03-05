package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.sharp.frc.team3260.RecycleRush.commands.*;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

import java.io.FileReader;
import java.util.HashMap;

public class ScriptedAutonomous
{
    private static final Log log = new Log("ScriptedAutonomous", Log.ATTRIBUTE_TIME);

    private CommandGroup commandGroup;

    private boolean commandsLoaded;
    private String pathToJSON;

    public ScriptedAutonomous()
    {
        load();
    }

    private boolean loadJSON()
    {
        commandGroup = new CommandGroup();

        JSONParser parser = new JSONParser();

        JSONObject jsonObject;

        int numCommandsAdded = 0;

        log.info(pathToJSON);

        try
        {
            jsonObject = (JSONObject) parser.parse(new FileReader("/U/Autonomous/" + pathToJSON));
        }
        catch(Exception e)
        {
            log.error("JSON File not Found, exception: " + e.toString());

            return false;
        }

        JSONArray commandList = (JSONArray) jsonObject.get("Commands");

        for(Object objCurrentCommand : commandList)
        {
            JSONObject currentCommand = (JSONObject) objCurrentCommand;
            String commandClass = (String) currentCommand.get("Command");
            JSONArray parameters = (JSONArray)currentCommand.get("Parameters");

            //Identify each paramter by their name.
            HashMap<String, JSONObject> parametersMap = new HashMap<String, JSONObject>();
            for (Object parameter : parameters) 
            {
                JSONObject currentParameter = (JSONObject) parameter;
                parametersMap.put(currentParameter.get("Name").toString(), currentParameter);
            }
            numCommandsAdded++;
            int level,time,timeout;
            double speed, distance, angle;

            if(commandClass == null || commandClass.equals(""))
            {
                continue;
            }

            switch(commandClass)
            {
                case "DriveDistanceCommand":
                    distance =  (double)parametersMap.get("Distance").get("Value");
                    timeout =   new Long((long)parametersMap.get("Timeout").get("Value")).intValue();

                    addSequential(new DriveDistanceCommand(distance,timeout));
                    break;

                case "DriveAtSpeedCommand":
                    time =    	new Long ((long) parametersMap.get("Time").get("Value")).intValue();
                    speed =     (double)parametersMap.get("Speed").get("Value");

                    addSequential(new DriveAtSpeedCommand(speed, time));

                    break;

                case "RotateToHeadingCommand":
                    angle =  (double)parametersMap.get("Degrees to Rotate").get("Value");
                    timeout = new Long((long) parametersMap.get("Timeout").get("Value")).intValue();

                    addSequential(new RotateToHeadingCommand(angle,timeout, true));

                    break;

                case "OpenGripperCommand":
                    addSequential(new OpenGripperCommand());
                    break;

                case "CloseGripperCommand":
                    addSequential(new CloseGripperCommand());
                    break;

                case "RobotIdleCommand":
                    //in milliseconds

                    time =  new Long((long)parametersMap.get("Time").get("Value")).intValue();
                    addSequential(new RobotIdleCommand(time));
                    break;

                case "ElevatorToSetpointCommand":
                    level = new Long((long) parametersMap.get("Level").get("Value")).intValue();
                    addSequential(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(level)));
                    break;

                case "ZeroGyroCommand":
                    addSequential(new ZeroGyroCommand());
                    break;

                default:
                    log.warn("Invalid command specified " + commandClass + ".");
                    break;
            }
        }

        getLog().info("Added " + numCommandsAdded + " Commands.");

        return true;
    }

    public String getPathToJSON()
    {
        return pathToJSON;
    }

    public void setPathToJSON(String path)
    {
        if(!pathToJSON.equals(path))
        {
            pathToJSON = path;

            load();
        }
    }

    public void setPathToCSV(String path, boolean forced)
    {
        if(!pathToJSON.equals(path) || forced)
        {
            pathToJSON = path;

            load();
        }
    }

    public void load()
    {
        if(pathToJSON == null)
        {
            log.warn("pathToJSON is null, setting Autonomous to BasicAutonomousCommandGroup.");

            pathToJSON = BasicAutonomousCommandGroup.class.getSimpleName();
        }

        if(pathToJSON.equals(BasicAutonomousCommandGroup.class.getSimpleName()))
        {
            log.warn("User asked for BasicAutonomousCommandGroup.");

            commandGroup = new BasicAutonomousCommandGroup();

            commandsLoaded = true;
        }
        else
        {
            commandsLoaded = loadJSON();
        }

        getLog().info("Autonomous loading was " + (commandsLoaded ? "successful." : "not successful."));

        if(!commandsLoaded)
        {
            getLog().info("Set command group to " + BasicAutonomousCommandGroup.class.getSimpleName() + ".");

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