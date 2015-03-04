package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.Constants;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.utils.Util;

public class ElevatorWithJoystickCommand extends Command
{
    public ElevatorWithJoystickCommand()
    {
        requires(Elevator.getInstance());
    }

    @Override
    protected void initialize()
    {
        Elevator.getInstance().stop();
    }

    @Override
    protected void execute()
    {
        if(Elevator.getInstance().getControlMode() == CANTalon.ControlMode.PercentVbus)
        {
            Elevator.getInstance().up(Util.handleDeadband(OI.getInstance().getManipulatorGamepad().getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_Y), Constants.joystickDeadband.getDouble()));
        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Elevator.getInstance().stop();
    }

    @Override
    protected void interrupted()
    {
        end();
    }
}
