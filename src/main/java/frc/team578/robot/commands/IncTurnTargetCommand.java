package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;

public class IncTurnTargetCommand extends Command {
	
	public int val = 0;
	public int inc = 1;
	public int mul = 256;
	public boolean fixed = false;
	boolean finished = false;
	
	public IncTurnTargetCommand() {
	}
	
	public IncTurnTargetCommand(int val) {
		this.val = val;
		this.fixed = true;
	}

	@Override
	protected void execute() {
		if (!fixed) {
			val = (mul * inc++) % 1024;
		}
		
		Robot.sds.setTurnMotorTargetEnc(val);
		
		finished = true;
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

}