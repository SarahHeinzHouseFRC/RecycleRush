package org.sharp.frc.team3260.RecycleRush.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import javafx.scene.effect.Light;
import org.sharp.frc.team3260.RecycleRush.Robot;
import org.sharp.frc.team3260.RecycleRush.subsystems.DriveTrain;
import org.sharp.frc.team3260.RecycleRush.subsystems.Elevator;
import org.sharp.frc.team3260.RecycleRush.subsystems.Lights;

import java.sql.Driver;

public class UpdateLightsCommand extends Command
{
    public UpdateLightsCommand()
    {
        requires(Lights.getInstance());
    }

    @Override
    protected void initialize()
    {
    }

    @Override
    protected void execute()
    {
        Lights.LightOption lightOption = Lights.LightOption.DEFAULT;

        double batteryVoltage = DriverStation.getInstance().getBatteryVoltage();

        double pressure = DriveTrain.getInstance().getPressure();

        byte elevatorPosition = Elevator.getInstance().getPositionAsByte();

        if(batteryVoltage < 11)
        {
            double batteryPercent = (batteryVoltage / 13);

            byte batteryPercentByte = (byte) (batteryPercent * Byte.MAX_VALUE);

            lightOption = Lights.LightOption.LOW_BATTERY;
            lightOption.setAdditionalData(batteryPercentByte);
        }
        else if(pressure < 40 && pressure > 10)
        {
            byte pressureAsByte = (byte) ((DriverStation.getInstance().getBatteryVoltage() / 120) * Byte.MAX_VALUE);

            lightOption = Lights.LightOption.LOW_PRESSURE;
            lightOption.setAdditionalData(pressureAsByte);
        }
        else if(elevatorPosition > 100)
        {
            lightOption = Lights.LightOption.ELEVATOR_STATUS;
            lightOption.setAdditionalData(Elevator.getInstance().getPositionAsByte());
        }

        Lights.getInstance().setLightMode(lightOption);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {

    }

    @Override
    protected void interrupted()
    {

    }
}
