package team2102.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team2102.robot.Robot;

@Deprecated
public class TestRotationCommand extends Command {

	private final double m_targetAngle;

	public TestRotationCommand(final double targetAngle) {
		requires(Robot.driveSubsystem);

		m_targetAngle = targetAngle;
	}

	@Override
	protected void execute() {
		Robot.driveSubsystem._testRotation(m_targetAngle);
		Robot.driveSubsystem._printRotatePosition();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
}
