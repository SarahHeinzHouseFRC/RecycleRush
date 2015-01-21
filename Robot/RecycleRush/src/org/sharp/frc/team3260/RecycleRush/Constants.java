package org.sharp.frc.team3260.RecycleRush;

import org.sharp.frc.team3260.RecycleRush.utils.ConstantsBase;

public class Constants extends ConstantsBase
{
	public static final Constant mainGamepadPort = new Constant("mainGamepadPort", 1);
	public static final Constant unimportantPersonsGamepadPort = new Constant("unimportantPersonsGamepadPort", 2);

	static
	{
		readConstantsFromFile();
	}

	private Constants()
	{
	}
}