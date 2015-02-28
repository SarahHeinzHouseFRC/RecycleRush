package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class Gripper extends SHARPSubsystem
{
    protected static Gripper instance;

    private Solenoid outSolenoid;
    private Solenoid inSolenoid;

    private DoubleSolenoid solenoid;

    public Gripper()
    {
        super("Gripper");

        instance = this;

        solenoid = new DoubleSolenoid(Constants.gripperSolenoidForwardChannel.getInt(), Constants.gripperSolenoidReverseChannel.getInt());
    }

    public void closeGripper()
    {
        getLogger().info("Closing");

        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void openGripper()
    {
        getLogger().info("Opening");

        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    @Override
    protected void initDefaultCommand()
    {
    }

    public static Gripper getInstance()
    {
        if(instance == null)
        {
            Robot.getInstance().getLogger().error("Something has gone horribly wrong in " + Gripper.class.getSimpleName());
        }

        return instance;
    }

    public boolean isClosed()
    {
        return solenoid.get().equals(DoubleSolenoid.Value.kReverse);
    }
}
