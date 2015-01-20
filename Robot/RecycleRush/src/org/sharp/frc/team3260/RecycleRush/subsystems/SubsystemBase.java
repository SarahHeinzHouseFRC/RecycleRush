package org.sharp.frc.team3260.RecycleRush.subsystems;

import java.util.Vector;

public class SubsystemBase
{
	private static SubsystemBase instance;

	private Vector<Subsystem> subsystems;

	public SubsystemBase()
	{
		instance = this;
	}

	public void addSubsystem(Subsystem subsystem)
	{
		for(Subsystem cur : subsystems)
		{
			if(subsystem.getClass().equals(cur.getClass()))
			{
				return;
			}
		}

		subsystems.add(subsystem);
	}

	public Subsystem getSubsystem(Class subsystem)
	{
		for(Subsystem cur : subsystems)
		{
			if(subsystem.equals(cur.getClass()))
			{
				return cur;
			}
		}

		return null; // Seems like a great idea?
	}

	public static SubsystemBase getInstance()
	{
		return instance;
	}
}
