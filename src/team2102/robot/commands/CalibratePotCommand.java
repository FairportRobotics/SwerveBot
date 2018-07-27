package team2102.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team2102.robot.Robot;

public class CalibratePotCommand extends InstantCommand {

	public CalibratePotCommand() {
		requires(Robot.driveSubsystem);
		setRunWhenDisabled(true);
	}

	@Override
	protected void initialize() {
		Robot.driveSubsystem.calibrateRotatePos();
	}

}
