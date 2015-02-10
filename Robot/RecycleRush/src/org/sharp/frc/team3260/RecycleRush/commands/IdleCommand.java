package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Created by NCS Customer on 2/9/2015.
 */
public class IdleCommand extends Command {

    private double timeToIdle;
    private Timer timer;

    public IdleCommand(double timeToIdle){this.timeToIdle = timeToIdle;}
    @Override
    protected void initialize(){
        timer.start();
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {

        return timer.hasPeriodPassed(timeToIdle);
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }
}
