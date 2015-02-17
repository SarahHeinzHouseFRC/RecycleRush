package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.sharp.frc.team3260.RecycleRush.OI;
import org.sharp.frc.team3260.RecycleRush.joystick.SHARPGamepad;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;

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
            Elevator.getInstance().up((Math.abs(OI.getInstance().getManipulatorGamepad().getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_Y)) > 0.1) ? -OI.getInstance().getManipulatorGamepad().getRawAxis(SHARPGamepad.JOYSTICK_RIGHT_Y) : 0.0f);
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
