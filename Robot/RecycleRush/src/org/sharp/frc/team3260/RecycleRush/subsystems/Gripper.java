package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.sharp.frc.team3260.RecycleRush.Constants;

public class Gripper extends SHARPSubsystem
{
    protected static Gripper instance;

    private DoubleSolenoid gripperSolenoid;

    public Gripper()
    {
        super("Gripper");

        instance = this;

        gripperSolenoid = new DoubleSolenoid(Constants.gripperSolenoidForwardChannel.getInt(), Constants.gripperSolenoidReverseChannel.getInt());
    }

    public void closeGripper()
    {
        gripperSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void openGripper()
    {
        gripperSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    @Override
    protected void initDefaultCommand()
    {
    }

    @Override
    protected void log()
    {
    }

    public static Gripper getInstance()
    {
        if (instance == null || instance.getClass() != Gripper.class)
        {
            System.out.println("Something has gone horribly wrong.");
        }

        return (Gripper) instance;
    }
}
