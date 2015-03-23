package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.SHARPIRRangeFinder;

public class Arms extends SHARPSubsystem
{
    protected static Arms instance;

    private DoubleSolenoid elevatorArmsSolenoid;

    private DoubleSolenoid lowerArmsSolenoid;

    private SHARPIRRangeFinder liftRangeFinder;
    private SHARPIRRangeFinder groundRangeFinder;

    public Arms()
    {
        super("Arms");

        instance = this;

        elevatorArmsSolenoid = new DoubleSolenoid(Constants.elevatorArmSolenoidForwardChannel.getInt(), Constants.elevatorArmsSolenoidReverseChannel.getInt());

        lowerArmsSolenoid = new DoubleSolenoid(Constants.lowerArmsSolenoidForwardChannel.getInt(), Constants.lowerArmsSolenoidReverseChannel.getInt());

        liftRangeFinder = new SHARPIRRangeFinder(Constants.liftInfraredSensorPort.getInt());
        groundRangeFinder = new SHARPIRRangeFinder(Constants.groundInfraredSensorPort.getInt());
    }

    public void closeElevatorArms()
    {
        getLogger().info("Closing Elevator Arms");

        elevatorArmsSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void openElevatorArms()
    {
        getLogger().info("Opening Elevator Arms");

        elevatorArmsSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void closeLowerArms()
    {
        getLogger().info("Closing Lower Arms");

        lowerArmsSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void openLowerArms()
    {
        getLogger().info("Opening Lower Arms");

        lowerArmsSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    @Override
    protected void initDefaultCommand()
    {
    }

    public void postRangeFinderValues()
    {
        SmartDashboard.putNumber("Lift Range Finder Inches", liftRangeFinder.getDistanceIn());

        SmartDashboard.putNumber("Ground Range Finder Inches", groundRangeFinder.getDistanceIn());
    }

    public static Arms getInstance()
    {
        if(instance == null)
        {
            Robot.getInstance().getLogger().error("Something has gone horribly wrong in " + Arms.class.getSimpleName());
        }

        return instance;
    }

    public boolean areElevatorArmsClosed()
    {
        return elevatorArmsSolenoid.get().equals(DoubleSolenoid.Value.kReverse);
    }

    public boolean areLowerArmsClosed()
    {
        return lowerArmsSolenoid.get().equals(DoubleSolenoid.Value.kForward);
    }
}
