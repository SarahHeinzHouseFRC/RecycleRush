package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class Subsystem extends edu.wpi.first.wpilibj.command.Subsystem
{
    public Subsystem()
    {
        super();

        initialize();
    }

    public Subsystem(String name)
    {
        super(name);

        initialize();
    }

    private void initialize()
    {
        SmartDashboard.putData(this);
    }

    protected abstract void initDefaultCommand();

    protected abstract void log();
}
