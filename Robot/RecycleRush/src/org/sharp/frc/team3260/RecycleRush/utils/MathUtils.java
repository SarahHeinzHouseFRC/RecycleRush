package org.sharp.frc.team3260.RecycleRush.utils;

import java.util.Random;

public final class MathUtils
{
	private static final Random RANDOM = new Random();

	// cannot be subclassed or instantiated
	private MathUtils() throws IllegalAccessException
	{
		throw new IllegalAccessException();
	}

	/**
	 * Limits the original value to a maximum value. This method will retain its
	 * +/- status.
	 * <p>
	 * <p>
	 * Ex. {@code 12, 1 returns 1}
	 * {@code -12, 1 returns -1}
	 * {@code 0.323, 3 returns 0.323}
	 *
	 * @param val   original value to limit
	 * @param limit absolute limit (does not have to be same sign as original
	 *              value)
	 * @return limited value of same sign
	 */
	public static double limit(double val, double limit)
	{
		return (Math.abs(val) < Math.abs(limit)) ? val : (val < 0 ? -limit : limit);
	}

	/**
	 * Returns the arithmetic mean of the array.
	 *
	 * @param vals values to find average of
	 * @return the mean value
	 */
	public static double mean(double[] vals)
	{
		double sum = 0;

		for(int x = 0; x < vals.length; x++)
		{
			sum += vals[x];
		}

		return sum / (double) vals.length;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed double value between 0.0
	 * and 1.0.
	 *
	 * @return a random double value
	 */
	public static double random()
	{
		return RANDOM.nextDouble();
	}

	/**
	 * Returns a pseudorandom integer. All 2^32 possible int values are produced
	 * with (approximately) equal probability.
	 *
	 * @return a random integer value
	 */
	public static int randomInt()
	{
		return RANDOM.nextInt();
	}

	public static double squareWave(double timeSec, double periodSec, double amplitude)
	{
		int periodMs = (int) (periodSec * 1000);

		int incrementTimeMs = (int) (timeSec * 1000) % periodMs;

		if(incrementTimeMs < periodMs / 2)
		{
			return amplitude;
		}

		return -amplitude;
	}
}