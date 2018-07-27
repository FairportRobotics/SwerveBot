package team2102.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team2102.robot.Robot;

@Deprecated
public class TestAccelCommand extends Command {

	public TestAccelCommand() {
		requires(Robot.driveSubsystem);
	}

	@Override
	protected void initialize() {
		Robot.driveSubsystem._startTestAccel();
	}

	@Override
	protected void execute() {
		Robot.driveSubsystem._runTestAccel();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.driveSubsystem._endTestAccel();
	}
}
