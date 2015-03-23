package org.sharp.frc.team3260.RecycleRush.joystick.triggers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import org.sharp.frc.team3260.RecycleRush.Robot;

public class HatButton extends Button
{
    private Joystick joystick;

    private int targetPos;

    public HatButton(Joystick joystick, int targetPos)
    {
        this.joystick = joystick;

        this.targetPos = targetPos;
    }

    @Override
    public boolean get()
    {
        return joystick.getPOV() == targetPos;
    }
}