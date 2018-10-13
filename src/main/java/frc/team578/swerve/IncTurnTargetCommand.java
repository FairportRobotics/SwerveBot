package frc.team578.swerve;

import edu.wpi.first.wpilibj.command.Command;

public class IncTurnTargetCommand extends Command {
	
	public static int val = 512;
	boolean finished = false;
	
	public IncTurnTargetCommand() {
	}
	
	@Override
	protected void execute() {
		val = (val + 256) % 1024;
		SwerveDrive.setTurnMotorTargetEnc(val);
		finished = true;
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

}
