package org.sharp.frc.team3260.RecycleRush.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.sharp.frc.team3260.RecycleRush.utils.logs.Log;

public abstract class SHARPSubsystem extends Subsystem
{
    protected Log log;

    public SHARPSubsystem()
    {
        super();

        initialize(this.getClass().getTypeName());
    }

    public SHARPSubsystem(String name)
    {
        super(name);

        initialize(name);
    }

    private void initialize(String name)
    {
        SmartDashboard.putData(this);

        log = new Log(name, Log.ATTRIBUTE_TIME);

        log.info("Created instance of Subsystem " + name + ".");
    }

    protected abstract void initDefaultCommand();

    public Log getLogger()
    {
        return log;
    }
}
