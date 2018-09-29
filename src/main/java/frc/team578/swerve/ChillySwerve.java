package frc.team578.swerve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wpi.first.wpilibj.Joystick;
import frc.team578.systems.PigeonGyro;
import frc.team578.utility.HardwareIDs;

public class ChillySwerve {

	private static final Logger logger = LogManager.getLogger(ChillySwerve.class);

	private static final double AUTO_DRIVE_ANGLE_CORRECT_COEFF = 0.02;
	private static final double GYRO_CORRECT_COEFF = 0.03;

	// used as angle baseline (if we don't reset gyro)
	private static double initialAngle = 0.0;
	private static double headingDeg = 0.0;

	// swerve units
	private static ChillySwerveUnit frontLeft, frontRight;
	private static ChillySwerveUnit backLeft, backRight;

	// swerve inputs
	private static Joystick driveGamepad;
	private static final double JOYSTICK_DEADZONE = 0.1;

	// drive values
	private static double fwd = 0.0;
	private static double str = 0.0;
	private static double rot = 0.0;

	// robot dimension ratios (units don't matter, just be consistent with all
	// three)
	private static final double l = 21; // drive base length
	private static final double w = 21; // drive base width
	private static final double r = Math.sqrt((l * l) + (w * w)); // diagonal drive base length

	// gyro angle
	private static double angleDeg = 0.0;

	// offsets
	private static boolean initialized = false;
	private static boolean offsetSet = false;
	public static final double FL_ABS_ZERO = 0.0;
	public static final double FR_ABS_ZERO = 0.0;
	public static final double BL_ABS_ZERO = 0.0;
	public static final double BR_ABS_ZERO = 0.0;

	public static void reset() {

		// make sure we're initialized
		initialize();

		// orient all wheels forward
		setAllLocation(0);

		// zero out all drive & turn motors
		setAllDrivePower(0);
		setAllTurnPower(0);
	}

	public static void initialize() {
		if (initialized)
			return;

		driveGamepad = new Joystick(HardwareIDs.CONTROL_GAMEPAD_ID);

		frontLeft = new ChillySwerveUnit(HardwareIDs.FRONT_LEFT_DRIVE_TALON_ID, HardwareIDs.FRONT_LEFT_ROTATE_TALON_ID);
		frontRight = new ChillySwerveUnit(HardwareIDs.FRONT_RIGHT_DRIVE_TALON_ID,
				HardwareIDs.FRONT_RIGHT_ROTATE_TALON_ID);
		backLeft = new ChillySwerveUnit(HardwareIDs.BACK_LEFT_DRIVE_TALON_ID, HardwareIDs.BACK_LEFT_ROTATE_TALON_ID);
		backRight = new ChillySwerveUnit(HardwareIDs.BACK_RIGHT_DRIVE_TALON_ID, HardwareIDs.BACK_RIGHT_ROTATE_TALON_ID);

		angleDeg = PigeonGyro.getAngle();

		initialized = true;
	}

	public static void autoInit(boolean resetGyro, double headingDeg, boolean magicMotion) {
		// set all wheels forward, motors off
		reset();

		if (resetGyro) {
			PigeonGyro.reset();
			initialAngle = 0.0;
		} else
			// initialAngle = NavXSensor.getAngle();
			initialAngle = headingDeg; // target heading if not resetting gyro
	}

	public static void autoStop() {
		reset();
	}

	public static void teleopInit() {
		reset();
	}

	public static void teleopPeriodic() {
		double joyVal;

		// get joystick inputs
		joyVal = driveGamepad.getRawAxis(HardwareIDs.LEFT_Y_AXIS);
		fwd = (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;

		joyVal = driveGamepad.getRawAxis(HardwareIDs.LEFT_X_AXIS);
		str = (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;

		joyVal = driveGamepad.getRawAxis(HardwareIDs.RIGHT_X_AXIS);
		rot = (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;

//		logger.debug(String.format("fwd %.2f str %.2f rot %.2f", fwd, str, rot));

//		fieldCentricDrive(fwd, str, rot);

		// debug only
//    InputOutputComm.putDouble(
//        InputOutputComm.LogTable.kMainLog, "ChillySwerve/FL_absAngle", frontLeft.getAbsAngle());
//    InputOutputComm.putDouble(
//        InputOutputComm.LogTable.kMainLog, "ChillySwerve/FR_absAngle", frontRight.getAbsAngle());
//    InputOutputComm.putDouble(
//        InputOutputComm.LogTable.kMainLog, "ChillySwerve/BL_absAngle", backLeft.getAbsAngle());
//    InputOutputComm.putDouble(
//        InputOutputComm.LogTable.kMainLog, "ChillySwerve/BR_absAngle", backRight.getAbsAngle());

//		logger.debug(String.format("fla %.2f fra %.2f rla %.2f rra %.2f", frontLeft.getAbsAngle(),
//				frontRight.getAbsAngle(), backLeft.getAbsAngle(), backRight.getAbsAngle()));
		
		logger.debug(String.format("fra :" + frontRight));
		
		frontRight.setTurnPower(rot);
		/*
		 * joyVal = driveGamepad.getRawAxis(HardwareIDs.LEFT_Y_AXIS); double left =
		 * (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;
		 * 
		 * joyVal = driveGamepad.getRawAxis(HardwareIDs.RIGHT_Y_AXIS); double right =
		 * (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;
		 * 
		 * tankDrive(-left, -right);
		 */
	}

	public static void disabledInit() {
		reset();
	}

	public static void disabledPeriodic() {
	}

	private static double angleToLoc(double angle) {
		if (angle < 0) {
			return .5d + ((180d - Math.abs(angle)) / 360d);
		} else {
			return angle / 360d;
		}
	}

	public static void swerveDrive(double fwd, double str, double rot) {
		double a = str - (rot * (l / r));
		double b = str + (rot * (l / r));
		double c = fwd - (rot * (w / r));
		double d = fwd + (rot * (w / r));

		double ws1 = Math.sqrt((b * b) + (c * c));
		double ws2 = Math.sqrt((b * b) + (d * d));
		double ws3 = Math.sqrt((a * a) + (d * d));
		double ws4 = Math.sqrt((a * a) + (c * c));

		double wa1 = Math.atan2(b, c) * 180 / Math.PI;
		double wa2 = Math.atan2(b, d) * 180 / Math.PI;
		double wa3 = Math.atan2(a, d) * 180 / Math.PI;
		double wa4 = Math.atan2(a, c) * 180 / Math.PI;

		double max = ws1;
		max = Math.max(max, ws2);
		max = Math.max(max, ws3);
		max = Math.max(max, ws4);
		if (max > 1) {
			ws1 /= max;
			ws2 /= max;
			ws3 /= max;
			ws4 /= max;
		}

		// ws1..ws4 and wa1..wa4 are the wheel speeds and wheel angles for wheels 1
		// through 4
		// which are front_right, front_left, rear_left, and rear_right, respectively.

//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/FL_pwr", ws2);
//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/FR_pwr", ws1);
//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/BL_pwr", ws3);
//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/BR_pwr", ws4);
//
//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/FL_angle", wa2);
//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/FR_angle", wa1);
//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/BL_angle", wa3);
//    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "ChillySwerve/BR_angle", wa4);

//		setDrivePower(ws2, ws1, ws3, ws4);

//		setLocation(0, 0, 0, 0);

//		setLocation(angleToLoc(wa2), angleToLoc(wa1), angleToLoc(wa3), angleToLoc(wa4));
	}

	public static void humanDrive(double fwd, double str, double rot) {
		if (Math.abs(rot) < 0.01)
			rot = 0;

		if (Math.abs(fwd) < .15 && Math.abs(str) < .15 && Math.abs(rot) < 0.01) {
			setDriveBrakeMode(true);
			stopDrive();
		} else {
			setDriveBrakeMode(false);
			swerveDrive(fwd, str, rot);
		}
	}

	public static void fieldCentricDrive(double fwd, double str, double rot) {

		double angleRad = PigeonGyro.getAngle() * (Math.PI / 180d);

		double temp = (fwd * Math.cos(angleRad)) + (str * Math.sin(angleRad));
		str = (-fwd * Math.sin(angleRad)) + (str * Math.cos(angleRad));
		fwd = temp;
		humanDrive(fwd, str, rot);
	}

	public static void resetAllEnc() {
		logger.debug("Reset All Enc");
		frontLeft.resetTurnEnc();
		frontRight.resetTurnEnc();
		backLeft.resetTurnEnc();
		backRight.resetTurnEnc();
	}

	public static void stopDrive() {
		logger.debug("Stop Drive");
		frontLeft.stopDrive();
		frontRight.stopDrive();
		backLeft.stopDrive();
		backRight.stopDrive();
	}

	public static void setDriveBrakeMode(boolean brake) {
		logger.debug("Drive Brake Mode Set : " + brake);
		frontLeft.setBrakeMode(brake);
		frontRight.setBrakeMode(brake);
		backLeft.setBrakeMode(brake);
		backRight.setBrakeMode(brake);
	}

	public static void setDrivePower(double fl, double fr, double bl, double br) {
		logger.debug("Drive Power Set : " + String.format("fl %.2f fr %.2f bl %.2f br %.2f", fl, fr, bl, br));
		frontLeft.setDrivePower(fl);
		frontRight.setDrivePower(fr);
		backLeft.setDrivePower(bl);
		backRight.setDrivePower(br);
	}

	public static void setTurnPower(double fl, double fr, double bl, double br) {
		logger.debug("Turn Power Set : " + String.format("fl %.2f fr %.2f bl %.2f br %.2f", fl, fr, bl, br));
		frontLeft.setTurnPower(fl);
		frontRight.setTurnPower(fr);
		backLeft.setTurnPower(bl);
		backRight.setTurnPower(br);
	}

	public static void setLocation(double fl, double fr, double bl, double br) {
		logger.debug("Location Set : " + String.format("fl %.2f fr %.2f bl %.2f br %.2f", fl, fr, bl, br));
		frontLeft.setTargetAngle(fl);
		frontRight.setTargetAngle(fr);
		backLeft.setTargetAngle(bl);
		backRight.setTargetAngle(br);
	}

	public static void setAllTurnPower(double power) {
		setTurnPower(power, power, power, power);
	}

	public static void setAllDrivePower(double power) {
		setDrivePower(power, power, power, power);
	}

	public static void setAllLocation(double loc) {
		setLocation(loc, loc, loc, loc);
	}
}
