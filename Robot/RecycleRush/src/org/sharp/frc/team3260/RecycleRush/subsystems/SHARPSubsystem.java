package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class SHARPSubsystem extends Subsystem
{
    protected static SHARPSubsystem instance;

    public SHARPSubsystem()
    {
        super();

        initialize();
    }

    public SHARPSubsystem(String name)
    {
        super(name);

        initialize();
    }

    private void initialize()
    {
        SmartDashboard.putData(this);

        instance = this;
    }

    protected abstract void initDefaultCommand();

    protected abstract void log();
}
