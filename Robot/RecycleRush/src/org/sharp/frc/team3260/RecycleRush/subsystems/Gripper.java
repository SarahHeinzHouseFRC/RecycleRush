package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.sharp.frc.team3260.RecycleRush.Constants;

public class Gripper extends SHARPSubsystem
{
    private DoubleSolenoid gripperSolenoid;

    public Gripper()
    {
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
        return (Gripper) instance;
    }
}
