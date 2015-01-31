package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.commands.ChainlifterStopCommand;

/**
 * TODO: ChainLifter speed controllers
 * TODO: ChainLifter sensors
 * TODO: ChainLifter control methods
 * TODO: ChainLifter operator interface
 * TODO: ChainLifter automation commands
 */
public class ChainLifter extends SHARPSubsystem
{
    protected CANTalon chainLiftMotor;

    public ChainLifter()
    {
        chainLiftMotor = new CANTalon(Constants.chainLiftMotor.getInt());
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new ChainlifterStopCommand());
    }

    public void up()
    {
        setChainlift(0.5);
    }

    public void down()
    {
        setChainlift(-0.5);
    }

    public void stop()
    {
        setChainlift(0.0);
    }

    private void setChainlift(double value)
    {
        chainLiftMotor.set(value);
    }


    @Override
    protected void log()
    {

    }

    public static ChainLifter getInstance()
    {
        return (ChainLifter) instance;
    }
}