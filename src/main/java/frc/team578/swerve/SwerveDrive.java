package frc.team578.swerve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.Robot;
import frc.team578.robot.RobotMap;
import frc.team578.systems.PigeonGyro;

public class SwerveDrive {

	private static final Logger logger = LogManager.getLogger(SwerveDrive.class);

	private static final double AUTO_DRIVE_ANGLE_CORRECT_COEFF = 0.02;
	private static final double GYRO_CORRECT_COEFF = 0.03;

	// used as angle baseline (if we don't reset gyro)
	private static double initialAngle = 0.0;
	private static double headingDeg = 0.0;

	// swerve units
	private static SwerveDriveUnit frontLeft, frontRight;
	private static SwerveDriveUnit backLeft, backRight;

	// swerve inputs
	private static final double JOYSTICK_DEADZONE = 0.1;

	// drive values
	private static double fwd = 0.0;
	private static double str = 0.0;
	private static double rot = 0.0;

	// robot dimension ratios (units don't matter, just be consistent with all
	// three)
	private static final double l = .625; // drive base length
	private static final double w = .47; // drive base width
	private static final double r = Math.sqrt((l * l) + (w * w)); // diagonal
																	// drive
																	// base
																	// length

	// gyro angle
	private static double angleDeg = 0.0;

	// offsets
	private static boolean initialized = false;
	public static final double FL_ABS_ZERO = 0.0;
	public static final double FR_ABS_ZERO = 0.0;
	public static final double BL_ABS_ZERO = 0.0;
	public static final double BR_ABS_ZERO = 0.0;

	public static void reset() {

		// make sure we're initialized
		initialize();

		// orient all wheels forward
		setAllTurnAngles(0);

		// zero out all drive & turn motors
		setAllDrivePower(0);
		setAllTurnPower(0);
	}

	public static void initialize() {
		if (initialized)
			return;

		frontLeft = new SwerveDriveUnit("frontLeft", RobotMap.FRONT_LEFT_DRIVE_TALON_ID,
				RobotMap.FRONT_LEFT_ROTATE_TALON_ID, RobotMap.FRONT_LEFT_REVERSE_DRIVE,
				RobotMap.FRONT_LEFT_REVERSE_TURN);

		frontRight = new SwerveDriveUnit("frontRight", RobotMap.FRONT_RIGHT_DRIVE_TALON_ID,
				RobotMap.FRONT_RIGHT_ROTATE_TALON_ID, RobotMap.FRONT_RIGHT_REVERSE_DRIVE,
				RobotMap.FRONT_RIGHT_REVERSE_TURN);

		backLeft = new SwerveDriveUnit("backLeft", RobotMap.BACK_LEFT_DRIVE_TALON_ID,
				RobotMap.BACK_LEFT_ROTATE_TALON_ID, RobotMap.BACK_LEFT_REVERSE_DRIVE, RobotMap.BACK_LEFT_REVERSE_TURN);

		backRight = new SwerveDriveUnit("backRight", RobotMap.BACK_RIGHT_DRIVE_TALON_ID,
				RobotMap.BACK_RIGHT_ROTATE_TALON_ID, RobotMap.BACK_RIGHT_REVERSE_DRIVE,
				RobotMap.BACK_RIGHT_REVERSE_TURN);

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
		joyVal = Robot.driveGamepad.getRawAxis(RobotMap.LEFT_Y_AXIS);
		fwd = (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;

		joyVal = Robot.driveGamepad.getRawAxis(RobotMap.LEFT_X_AXIS);
		str = (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;

		joyVal = Robot.driveGamepad.getRawAxis(RobotMap.RIGHT_X_AXIS);
		rot = (Math.abs(joyVal) > JOYSTICK_DEADZONE) ? joyVal : 0.0;

		// The joystick forward is negative for some reason.
		fwd *= -1;

//		logger.debug(String.format("fwd %.2f str %.2f rot %.2f", fwd, str, rot));

		// Uses Pigeon
		// fieldCentricDrive(fwd, str, rot);

		// Manually fwd/backwards
//		SwerveDrive.setAllDrivePower(fwd);

		// cw/ccw rotational from joystick
		manualRotateDrive(fwd, str, rot);

		SwerveDrive.updateDashboard();

	}

	public static void disabledInit() {
		reset();
	}

	public static void disabledPeriodic() {
	}

	public static void swerveDrive(double fwd, double str, double rot) {

		// ws1..ws4 and wa1..wa4 - wheels 1 through 4
		// which are front_right, front_left, rear_left, and rear_right,
		// respectively.
		// ether's spreadsheet is helpful for testing values here.

		double a = str - (rot * (l / r));
		double b = str + (rot * (l / r));
		double c = fwd - (rot * (w / r));
		double d = fwd + (rot * (w / r));

		// Speed
		double ws1 = Math.sqrt((b * b) + (c * c)); // fr
		double ws2 = Math.sqrt((b * b) + (d * d)); // fl
		double ws3 = Math.sqrt((a * a) + (d * d)); // rl
		double ws4 = Math.sqrt((a * a) + (c * c)); // rr

		// Angle
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

		logger.info(String.format("ws1:%.2f, ws2:%.2f, ws3:%.2f, ws4:%.2f", ws1, ws2, ws3, ws4));
		logger.info(String.format("wa1:%.2f, wa2:%.2f, wa3:%.2f, wsa:%.2f", wa1, wa2, wa3, wa4));

		SmartDashboard.putNumber("ws1", ws1);
		SmartDashboard.putNumber("ws2", ws2);
		SmartDashboard.putNumber("ws3", ws3);
		SmartDashboard.putNumber("ws4", ws4);

		setDrivePower(ws2, ws1, ws3, ws4);

		SmartDashboard.putNumber("wa1", wa1);
		SmartDashboard.putNumber("wa2", wa2);
		SmartDashboard.putNumber("wa3", wa3);
		SmartDashboard.putNumber("wa4", wa4);

		double na1 = normalizeAngle(wa1);
		double na2 = normalizeAngle(wa2);
		double na3 = normalizeAngle(wa3);
		double na4 = normalizeAngle(wa4);

		SmartDashboard.putNumber("na1", na1);
		SmartDashboard.putNumber("na2", na2);
		SmartDashboard.putNumber("na3", na3);
		SmartDashboard.putNumber("na4", na4);

		setTurnAngle(na2, na1, na3, na4);

	}

	private static double normalizeAngle(double angle) {

		// What part of the compass?
		double mul = 0;
		if (angle < 0) {
			mul = .5d + ((180d - Math.abs(angle)) / 360d);
		} else {
			mul = angle / 360d;
		}

		return 360d * mul;

	}

	public static void manualRotateDrive(double fwd, double str, double rot) {
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
		manualRotateDrive(fwd, str, rot);
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

	public static void setTurnAngle(double flAngle, double frAngle, double blAngle, double brAngle) {
		logger.info("Location Set : "
				+ String.format("fl %.2f fr %.2f bl %.2f br %.2f", flAngle, frAngle, blAngle, brAngle));

		double fltpos = frontLeft.calculateTargetEncoderPos(flAngle);
		double frtpos = frontRight.calculateTargetEncoderPos(frAngle);
		double bltpos = backLeft.calculateTargetEncoderPos(blAngle);
		double brtpos = backRight.calculateTargetEncoderPos(brAngle);

//		double flta = frontLeft.calculateFancyTargetEncoderPos(flAngle);
//		double frta = frontRight.calculateFancyTargetEncoderPos(frAngle);
//		double blta = backLeft.calculateFancyTargetEncoderPos(blAngle);
//		double brta = backRight.calculateFancyTargetEncoderPos(brAngle);

		SmartDashboard.putNumber("FL TPOS", fltpos);
		SmartDashboard.putNumber("FR TPOS", frtpos);
		SmartDashboard.putNumber("BL TPOS", bltpos);
		SmartDashboard.putNumber("BR TPOS", brtpos);

		frontLeft.setTurnMotorTargetEnc(fltpos);
		frontRight.setTurnMotorTargetEnc(frtpos);
		backLeft.setTurnMotorTargetEnc(bltpos);
		backRight.setTurnMotorTargetEnc(brtpos);

//		frontLeft.setTurnMotorTargetEnc(fl);
//		frontRight.setTurnMotorTargetEnc(fr);
//		backLeft.setTurnMotorTargetEnc(bl);
//		backRight.setTurnMotorTargetEnc(br);
	}

	public static void setTurnMotorTargetEnc(int encVal) {
		frontLeft.setTurnMotorTargetEnc(encVal);
		frontRight.setTurnMotorTargetEnc(encVal);
		backLeft.setTurnMotorTargetEnc(encVal);
		backRight.setTurnMotorTargetEnc(encVal);
	}

	// Will tell the talon to make current swerve position be 0
	// Use to to calibrate wheel position with the talon encoder
	public static void resetAllTurnEncoders() {
		frontLeft.resetTurnEnc();
		frontRight.resetTurnEnc();
		backLeft.resetTurnEnc();
		backLeft.resetTurnEnc();
	}

	public static void setAllTurnPower(double power) {
		setTurnPower(power, power, power, power);
	}

	public static void setAllDrivePower(double power) {
		setDrivePower(power, power, power, power);
	}

	public static void setAllTurnAngles(double angle) {
		setTurnAngle(angle, angle, angle, angle);
	}

	public static void updateDashboard() {
		SmartDashboard.putNumber("FL Enc Pos", frontLeft.getTurnEncPos());
		SmartDashboard.putNumber("FR Enc Pos", frontRight.getTurnEncPos());
		SmartDashboard.putNumber("BL Enc Pos", backLeft.getTurnEncPos());
		SmartDashboard.putNumber("BR Enc Pos", backRight.getTurnEncPos());

		SmartDashboard.putNumber("FL CLT", frontLeft.getTurnCLT(0));
		SmartDashboard.putNumber("FR CLT", frontRight.getTurnCLT(0));
		SmartDashboard.putNumber("BL CLT", backLeft.getTurnCLT(0));
		SmartDashboard.putNumber("BR CLT", backRight.getTurnCLT(0));

		SmartDashboard.putNumber("FL CLTERR", frontLeft.getTurnCLTError(0));
		SmartDashboard.putNumber("FR CLTERR", frontRight.getTurnCLTError(0));
		SmartDashboard.putNumber("BL CLTERR", backLeft.getTurnCLTError(0));
		SmartDashboard.putNumber("BR CLTERR", backRight.getTurnCLTError(0));

		SmartDashboard.putNumber("FL AbsAng", frontLeft.getAbsAngle());
		SmartDashboard.putNumber("FR AbsAng", frontRight.getAbsAngle());
		SmartDashboard.putNumber("BL AbsAng", backLeft.getAbsAngle());
		SmartDashboard.putNumber("BR AbsAng", backRight.getAbsAngle());

//		SmartDashboard.putNumber("FL DPower", frontLeft.get());
//		SmartDashboard.putNumber("FR DPower", frontRight.getAbsAngle());
//		SmartDashboard.putNumber("BL DPower", backLeft.getAbsAngle());
//		SmartDashboard.putNumber("BR DPower", backRight.getAbsAngle());

		SmartDashboard.putNumber("Foward", fwd);
		SmartDashboard.putNumber("Strafe", str);
		SmartDashboard.putNumber("Rotate", rot);

		SmartDashboard.putString("FL", frontLeft.toString());
		SmartDashboard.putString("FR", frontRight.toString());
		SmartDashboard.putString("BL", backLeft.toString());
		SmartDashboard.putString("BR", backRight.toString());

		SmartDashboard.putNumber("Inc", IncTurnTargetCommand.val);

	}

}
