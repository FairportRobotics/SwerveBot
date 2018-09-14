package team578.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team578.robot.Robot;

public class ResetGyroCommand extends InstantCommand {

	public ResetGyroCommand() {
		requires(Robot.driveSubsystem);
		setRunWhenDisabled(true);
	}

	@Override
	protected void initialize() {
//		Robot.driveSubsystem.resetHeading();
	}

}
