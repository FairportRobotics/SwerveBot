package frc.team578.swerve;

import edu.wpi.first.wpilibj.command.PIDCommand;

public class AutoTurnToPosCommand extends PIDCommand {
	
	public AutoTurnToPosCommand(double p, double i, double d) {
		super(p, i, d);
	}

	private static final double kP = .1;
	private static final double kI = 0.0;
	private static final double kD = 0.25;

	private final double kToleranceDegrees = 2f;

	private boolean hasRunReturnPidInputAtLeastOnce;

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
