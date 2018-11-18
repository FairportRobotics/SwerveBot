package frc.team578.swerve.drive;

import java.util.List;

import frc.team578.swerve.math.CentricMode;
import frc.team578.swerve.math.SwerveDirective;
import frc.team578.swerve.math.SwerveMath;

/**
 * The main class for the SwerveDrive subsystem: This class handles all aspects
 * of controlling the swerve drive. Use this class in your program if you want
 * the easiest way to integrate swerve drive into your robot.
 */
public class SwerveDrive {
	// Enclosures 1-4 are the drive/steer combos
	private SwerveEnclosure swerveEnclosureFR;
	private SwerveEnclosure swerveEnclosureFL;
	private SwerveEnclosure swerveEnclosureBL;
	private SwerveEnclosure swerveEnclosureBR;

	private final SwerveMath swerveMath;


	public SwerveDrive(SwerveEnclosure swerveEnclosureFL, SwerveEnclosure swerveEnclosureFR,
			SwerveEnclosure swerveEnclosureBL, SwerveEnclosure swerveEnclosureBR, double width, double length) {

		this.swerveEnclosureFR = swerveEnclosureFR;
		this.swerveEnclosureFL = swerveEnclosureFL;
		this.swerveEnclosureBL = swerveEnclosureBL;
		this.swerveEnclosureBR = swerveEnclosureBR;

		// instantiate the swerve library with a gyro provider using pigeon1
		swerveMath = new SwerveMath(width, length);

	}

	/**
	 * move Moves the robot based on 3 inputs - fwd (forward), str(strafe), and
	 * rcw(rotation clockwise) Inputs are between -1 and 1, with 1 being full power,
	 * -1 being full reverse, and 0 being neutral. The method uses gyro for field
	 * centric driving, if it is enabled.
	 *
	 * @param fwd
	 * @param str
	 * @param rcw
	 * @param gyroValue the value of the gyro input to be used by the calculation.
	 *                  Optional. Only used when the robot is in field-centric mode.
	 */
	public void move(double fwd, double str, double rcw, Double gyroValue) {
		// Get the move command calculated
		List<SwerveDirective> swerveDirectives = swerveMath.move(fwd, str, rcw, gyroValue);

		swerveEnclosureFR.move(swerveDirectives.get(0).getSpeed(), swerveDirectives.get(0).getAngle());
		swerveEnclosureFL.move(swerveDirectives.get(1).getSpeed(), swerveDirectives.get(1).getAngle());
		swerveEnclosureBL.move(swerveDirectives.get(2).getSpeed(), swerveDirectives.get(2).getAngle());
		swerveEnclosureBR.move(swerveDirectives.get(3).getSpeed(), swerveDirectives.get(3).getAngle());
	}

	/**
	 * Stop the robot (set speed to 0)
	 * 
	 * @throws Exception
	 */
	public void stop() {
		swerveEnclosureFR.stop();
		swerveEnclosureFL.stop();
		swerveEnclosureBL.stop();
		swerveEnclosureBR.stop();
	}

	/**
	 * Change the centric-mode of the robot (this can be done dynamically any time
	 * and will affect the robot behavior from that point on)
	 */
	public void setCentricMode(CentricMode centricMode) {
		this.swerveMath.setCentricMode(centricMode);
	}

	public void setModeField() {
		this.swerveMath.setModeField();
	}

	

}
