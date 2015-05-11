package org.sharp.frc.team3260.RecycleRush.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
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
            JSONArray parameters = (JSONArray) currentCommand.get("Parameters");

            //Identify each parameter by their name.
            HashMap<String, JSONObject> parametersMap = new HashMap<>();
            for(Object parameter : parameters)
            {
                JSONObject currentParameter = (JSONObject) parameter;
                parametersMap.put(currentParameter.get("Name").toString(), currentParameter);
            }
            numCommandsAdded++;
            int level, timeout;
            double speed, distance, angle, time;

            if(commandClass == null || commandClass.equals(""))
            {
                continue;
            }

            boolean isParallel = false;

            switch(commandClass)
            {
                case "DriveDistanceCommand":
                    distance = (double) parametersMap.get("Distance").get("Value");
                    timeout = new Long((long) parametersMap.get("Timeout").get("Value")).intValue();

                    addCommand(new DriveDistanceCommand(distance, timeout), isParallel);
                    break;

                case "DriveAtSpeedCommand":
                    time = (new Long((long) parametersMap.get("Time").get("Value")) / (double) 1000);
                    speed = (double) parametersMap.get("Speed").get("Value");

                    addCommand(new DriveAtSpeedCommand(speed, time), isParallel);
                    break;

                case "RotateToHeadingCommand":
                    angle = (double) parametersMap.get("Degrees to Rotate").get("Value");
                    timeout = new Long((long) parametersMap.get("Timeout").get("Value")).intValue();

                    addCommand(new RotateToHeadingCommand(angle, timeout, true), isParallel);
                    break;

                case "OpenElevatorArmsCommand":
                    addCommand(new OpenElevatorArmsCommand(), isParallel);
                    break;

                case "CloseElevatorArmsCommand":
                    addCommand(new CloseElevatorArmsCommand(), isParallel);
                    break;

                case "OpenLowerArmsCommand":
                    addCommand(new OpenLowerArmsCommand(), isParallel);
                    break;

                case "CloseLowerArmsCommand":
                    addCommand(new CloseLowerArmsCommand(), isParallel);
                    break;

                case "RobotIdleCommand":
                    //in milliseconds
                    time = (new Long((long) parametersMap.get("Time").get("Value")) / (double) 1000);

                    addCommand(new RobotIdleCommand(time), isParallel);
                    break;

                case "ElevatorToSetpointCommand":
                    level = new Long((long) parametersMap.get("Level").get("Value")).intValue();

                    addCommand(new ElevatorToSetpointCommand(Elevator.ElevatorPosition.getPositionByIndex(level)), isParallel);
                    break;

                case "ZeroGyroCommand":
                    addCommand(new ZeroGyroCommand(), isParallel);
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

    public void load()
    {
        if(pathToJSON == null)
        {
            log.warn("pathToJSON is null, setting Autonomous to DefaultAutonomousCommandGroup.");

            pathToJSON = DefaultAutonomousCommandGroup.class.getSimpleName();
        }

        if(pathToJSON.equals(DefaultAutonomousCommandGroup.class.getSimpleName()))
        {
            log.warn("User asked for DefaultAutonomousCommandGroup.");

            commandGroup = new DefaultAutonomousCommandGroup();

            commandsLoaded = true;
        }
        else if(pathToJSON.equals(ThreeToteAutonomousCommandGroup.class.getSimpleName()))
        {
            commandGroup = new ThreeToteAutonomousCommandGroup();
            commandsLoaded = true;
        }
        else if (pathToJSON.equals(CanAndLiftAutonomousCommand.class.getSimpleName()))
        {

            commandGroup = new CanAndLiftAutonomousCommand();
            commandsLoaded = true;
        }
        else
        {
            commandsLoaded = loadJSON();
        }

        DriverStation.getInstance().reportError("Autonomous loading was " + (commandsLoaded ? " succcesful" : "failed"), false);

        getLog().info("Autonomous loading was " + (commandsLoaded ? "successful." : "not successful."));

        if(!commandsLoaded)
        {
            getLog().info("Set command group to " + DefaultAutonomousCommandGroup.class.getSimpleName() + ".");

            commandGroup = new DefaultAutonomousCommandGroup();
        }
    }

    private void addCommand(Command command, boolean isParallel)
    {
        if(isParallel)
        {
            addParallel(command);
        }
        else
        {
            addSequential(command);
        }
    }

    private void addCommand(Command command, double timeout, boolean isParallel)
    {
        if(isParallel)
        {
            addParallel(command, timeout);
        }
        else
        {
            addSequential(command, timeout);
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

    private void addParallel(Command command)
    {
        commandGroup.addParallel(command);
    }

    private void addParallel(Command command, double timeout)
    {
        commandGroup.addParallel(command, timeout);
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