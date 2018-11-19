package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;

public class FlipDriveModeCommand extends Command {
	
	boolean finished = false;
	
	public FlipDriveModeCommand() {
	}

	@Override
	protected void execute() {
		Robot.sds.flipCentricMode();
		finished = true;
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

}
