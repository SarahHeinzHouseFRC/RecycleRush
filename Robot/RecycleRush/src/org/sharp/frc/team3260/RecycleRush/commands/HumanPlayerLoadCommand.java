package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.subsystems.Arms;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class HumanPlayerLoadCommand extends Command
{
    protected static final int STAGE_BEGIN = 0, STAGE_MOVINGTOREADY = 1, STAGE_READY = 2, STAGE_GRIPPED = 3, STAGE_LIFTING = 4, STAGE_FINISHED = 5;

    protected int currentStage;

    protected long stageStartTime;

    public HumanPlayerLoadCommand()
    {
        currentStage = STAGE_BEGIN;

        requires(Elevator.getInstance());
        requires(Arms.getInstance());
    }

    @Override
    protected void initialize()
    {
        setTimeout(5.0);

        if(!Arms.getInstance().areLowerArmsClosed())
        {
            Arms.getInstance().getLogger().info("Attempted to use HumanPlayerLoadCommand with lower arms open.");

            currentStage = STAGE_FINISHED;
        }

        currentStage = STAGE_BEGIN;

        Elevator.getInstance().changeElevatorMode(true);

        stageStartTime = System.currentTimeMillis();
    }

    @Override
    protected void execute()
    {
        switch(currentStage)
        {
            case STAGE_BEGIN:
                Elevator.getInstance().setElevator(Elevator.ElevatorPosition.TWO_TOTE);

                currentStage++;
                break;

            case STAGE_MOVINGTOREADY:
                if(Elevator.getInstance().atSetpoint())
                {
                    currentStage++;
                }
                break;

            case STAGE_READY:
                Arms.getInstance().closeElevatorArms();

                if(System.currentTimeMillis() - stageStartTime > 300)
                {
                    currentStage++;
                }
                break;

            case STAGE_GRIPPED:
                Elevator.getInstance().setElevator(Elevator.ElevatorPosition.THREE_TOTES);

                currentStage = STAGE_LIFTING;
                break;

            case STAGE_LIFTING:
                if(Elevator.getInstance().atSetpoint())
                {
                    currentStage = STAGE_FINISHED;
                }
                break;
        }
    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut() || (currentStage == STAGE_FINISHED);
    }

    @Override
    protected void end()
    {
        Arms.getInstance().openElevatorArms();

        Elevator.getInstance().changeElevatorMode(false);

        Robot.getInstance().getLogger().warn("HumanPlayerLoadCommand Ended");
    }

    @Override
    protected void interrupted()
    {
        Robot.getInstance().getLogger().warn("HumanPlayerLoadCommand Interrupted");
    }
}
