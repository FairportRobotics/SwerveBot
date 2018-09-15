package team578.robot.commands;

import java.util.Random;

import edu.wpi.first.wpilibj.command.Command;
import team578.robot.Robot;

//@Deprecated
public class TestRotationCommand extends Command {

	private final double m_targetAngle;

	public TestRotationCommand(final double targetAngle) {
		requires(Robot.driveSubsystem);
		m_targetAngle = targetAngle;
	}
	
	public TestRotationCommand() {
		requires(Robot.driveSubsystem);
		m_targetAngle = new Random().nextInt(359);
	}

	@Override
	protected void execute() {
		Robot.driveSubsystem.testRotation(m_targetAngle);
		Robot.driveSubsystem.printRotatePosition();
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		System.err.println("Test Rotate To : " + m_targetAngle);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
}
