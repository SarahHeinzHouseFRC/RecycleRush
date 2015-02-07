package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class Gripper extends SHARPSubsystem
{
    protected static Gripper instance;

    private Solenoid outSolenoid;
    private Solenoid inSolenoid;

    public Gripper()
    {
        super("Gripper");

        instance = this;

        outSolenoid = new Solenoid(Constants.gripperSolenoidForwardChannel.getInt());
        inSolenoid = new Solenoid(Constants.gripperSolenoidReverseChannel.getInt());
    }

    public void closeGripper()
    {
        outSolenoid.set(false);
        inSolenoid.set(true);
    }

    public void openGripper()
    {
        outSolenoid.set(true);
        inSolenoid.set(false);
    }

    @Override
    protected void initDefaultCommand()
    {
    }

    public static Gripper getInstance()
    {
        if (instance == null)
        {
            Robot.getInstance().getLogger().error("Something has gone horribly wrong in " + Gripper.class.getSimpleName());
        }

        return instance;
    }
}
