package frc.team578.swerve;

import edu.wpi.first.wpilibj.command.Command;

public class IncTurnTargetCommand extends Command {
	
	public static int val = 512;
	boolean finished = false;
	
	public IncTurnTargetCommand() {
		System.err.println("INC TURN");
	}
	
	@Override
	protected void execute() {
		val = (val + 256) % 1024;
		System.err.println("New VAL -> " + val);
		finished = true;
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

}
