package team578.robot;

import java.util.Random;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import team578.robot.commands.TestRotationCommand;

public class OI {

	public final Joystick stick = new Joystick(0);
	
	public OI() {
		int JOYSTICK_TRIGGER_BUTTON_NUMBER = 1;
		JoystickButton leftTrigger = new JoystickButton(stick, JOYSTICK_TRIGGER_BUTTON_NUMBER);
		
		leftTrigger.whenPressed(new TestRotationCommand());
	}
}
