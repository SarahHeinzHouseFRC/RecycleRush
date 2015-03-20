package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.subsystems.Arms;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

public class HumanPlayerLoadCommand extends Command
{
    protected static final int STAGE_BEGIN = 0, STAGE_GRIPPED = 1, STAGE_LIFTING = 3, STAGE_FINISHED = 4;

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
        setTimeout(1.0);

        if(!Arms.getInstance().areLowerArmsClosed())
        {
            Arms.getInstance().getLogger().info("Attempted to use HumanPlayerLoadCommand with lower arms open.");

            currentStage = STAGE_FINISHED;
        }

        currentStage = STAGE_BEGIN;

        stageStartTime = System.currentTimeMillis();
    }

    @Override
    protected void execute()
    {
        switch(currentStage)
        {
            case STAGE_BEGIN:
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
    }

    @Override
    protected void interrupted()
    {
    }
}