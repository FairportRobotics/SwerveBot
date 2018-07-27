package team2102.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import team2102.robot.Robot;

public class FollowTrajectoryCommand extends Command {

	private final EncoderFollower[] m_followers;

	public FollowTrajectoryCommand(final Waypoint[] points) {
		requires(Robot.driveSubsystem);

		m_followers = Robot.driveSubsystem.generateFollowers(points);
	}

	@Override
	protected void initialize() {
		Robot.driveSubsystem.startFollowing(m_followers);
	}

	@Override
	protected void execute() {
		// See isFinished()
	}

	@Override
	protected boolean isFinished() {
		return Robot.driveSubsystem.follow(m_followers);
	}

	@Override
	protected void end() {
		Robot.driveSubsystem.drive(0.0, 0.0);
	}
}
