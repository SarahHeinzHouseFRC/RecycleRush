package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.utils.MathUtils;

/**
 * @author Eddie
 */
public class SHARPDrive extends Command
{
	private double oldWheel = 0.0;

	private double quickStopAccumulator;

	public SHARPDrive()
	{
		requires(Robot.getDrivetrain());
	}

	protected void initialize()
	{
		// Robot.drivetrain.disableControllers();
	}

	protected void execute()
	{
		double leftOutput, rightOutput;
		double overPower;

		double sensitivity = 1.7;

		double angularPower, linearPower;

		double negInertiaAccumulator = 0.0, negInertiaScalar;

		boolean useQuickTurn = OI.getInstance().getMainGamepad().getButtonRightJoystick();

		double nonLinearity;

		double wheel;
		double throttle;

		wheel = handleDeadband(OI.getInstance().getMainGamepad().getJoystickRightX(), 0.05);
		throttle = handleDeadband(OI.getInstance().getMainGamepad().getJoystickLeftY(), 0.05);

		double negInertia = wheel - oldWheel;
		oldWheel = wheel;

		nonLinearity = 0.5;

		wheel = Math.sin(Math.PI / 2.0 * nonLinearity * wheel) / Math.sin(Math.PI / 2.0 * nonLinearity);
		wheel = Math.sin(Math.PI / 2.0 * nonLinearity * wheel) / Math.sin(Math.PI / 2.0 * nonLinearity);
		wheel = Math.sin(Math.PI / 2.0 * nonLinearity * wheel) / Math.sin(Math.PI / 2.0 * nonLinearity);

		if(wheel * negInertia > 0)
		{
			negInertiaScalar = 2.5;
		}
		else
		{
			if(Math.abs(wheel) > 0.65)
			{
				negInertiaScalar = 5.0;
			}
			else
			{
				negInertiaScalar = 3.0;
			}

			sensitivity = 0.75;
		}

		double negInertiaPower = negInertia * negInertiaScalar;
		negInertiaAccumulator += negInertiaPower;

		wheel = wheel + negInertiaAccumulator;

		if(negInertiaAccumulator > 1)
		{
			negInertiaAccumulator -= 1;
		}
		else if(negInertiaAccumulator < -1)
		{
			negInertiaAccumulator += 1;
		}
		else
		{
			negInertiaAccumulator = 0;
		}

		linearPower = throttle;

		if(useQuickTurn)
		{
			if(Math.abs(linearPower) < 0.2)
			{
				double alpha = 0.1;

				quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha * MathUtils.limit(wheel, 1.0) * 5;
			}

			overPower = 1.0;

			angularPower = wheel;
		}
		else
		{
			overPower = 0.0;

			angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;

			if(quickStopAccumulator > 1)
			{
				quickStopAccumulator -= 1;
			}
			else if(quickStopAccumulator < -1)
			{
				quickStopAccumulator += 1;
			}
			else
			{
				quickStopAccumulator = 0.0;
			}
		}

		rightOutput = leftOutput = linearPower;
		leftOutput += angularPower;
		rightOutput -= angularPower;

		if(leftOutput > 1.0)
		{
			rightOutput -= overPower * (leftOutput - 1.0);
			leftOutput = 1.0;
		}
		else if(leftOutput < -1.0)
		{
			rightOutput += overPower * (-1.0 - leftOutput);
			leftOutput = -1.0;
		}

		if(rightOutput < -1.0)
		{
			leftOutput += overPower * (-1.0 - rightOutput);
			rightOutput = -1.0;
		}
		else if(rightOutput > 1.0)
		{
			leftOutput -= overPower * (rightOutput - 1.0);
			rightOutput = 1.0;
		}

		Robot.getDrivetrain().tankDrive(leftOutput, rightOutput);
	}

	protected boolean isFinished()
	{
		return false;
	}

	protected void end()
	{
	}

	protected void interrupted()
	{
	}

	public double handleDeadband(double value, double deadband)
	{
		return (Math.abs(value) > Math.abs(deadband)) ? value : 0.0;
	}
}