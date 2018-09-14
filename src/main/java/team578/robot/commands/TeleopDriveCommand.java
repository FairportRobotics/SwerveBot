package team578.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import team578.robot.Robot;

public class TeleopDriveCommand extends Command {

	private static final double k_deadzone = 0.25;
	private static final double k_twistRate = 1.0; // 1 degree per main loop

	public TeleopDriveCommand() {
		requires(Robot.driveSubsystem);
		System.err.println(" Init Teleop TeleopDriveCommand");
	}

	@Override
	protected void execute() {
		System.err.println("Exec Teleop TeleopDriveCommand");
		
		final Joystick stick = Robot.oi.stick;

		final double mag = stick.getMagnitude();
		final double dir = stick.getDirectionDegrees();
		
		System.err.println(" Mag " + mag + " dir " + dir);

		double headingDelta = 0.0;
		if (stick.getRawButton(3)) {
			headingDelta = -k_twistRate;
		} else if (stick.getRawButton(4)) {
			headingDelta = +k_twistRate;
		}

		Robot.driveSubsystem.m_targetHeading = (Robot.driveSubsystem.m_targetHeading + headingDelta) % 360.0;

//		if (mag > k_deadzone) {
			Robot.driveSubsystem.drive(mag * mag * mag, dir);
//		} else {
//			Robot.driveSubsystem.drive(0.0, 0.0);
//		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
//		Robot.driveSubsystem.drive(0.0, 0.0);
	}
}
