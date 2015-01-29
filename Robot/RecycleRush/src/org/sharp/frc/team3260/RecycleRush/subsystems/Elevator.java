package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import org.sharp.frc.team3260.RecycleRush.Constants;

/**
 * TODO: Decide whether or not the Aidanvator gripper and Aidanvator should be in the same subsystem
 * TODO: Aidanvator sensors
 * TODO: Aidanvator control methods
 * TODO: Aidanvator operator interface
 * TODO: Aidanvator automation commands
 */
public class Elevator extends Subsystem
{
    private CANTalon aidanvatorCIM;

    public Elevator()
    {
        aidanvatorCIM = new CANTalon(Constants.aidanvatorCIM.getInt());
    }

    public void up()
    {
        setAidanvator(0.5);
    }

    public void down()
    {
        setAidanvator(-0.5);
    }

    public void stop()
    {
        setAidanvator(0.0);
    }

    private void setAidanvator(double value)
    {
        aidanvatorCIM.set(value);
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    @Override
    protected void log()
    {

    }
}
