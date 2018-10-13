package frc.team578.swerve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
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

		frontLeft = new SwerveDriveUnit(RobotMap.FRONT_LEFT_DRIVE_TALON_ID, RobotMap.FRONT_LEFT_ROTATE_TALON_ID);
		frontRight = new SwerveDriveUnit(RobotMap.FRONT_RIGHT_DRIVE_TALON_ID, RobotMap.FRONT_RIGHT_ROTATE_TALON_ID);
		backLeft = new SwerveDriveUnit(RobotMap.BACK_LEFT_DRIVE_TALON_ID, RobotMap.BACK_LEFT_ROTATE_TALON_ID);
		backRight = new SwerveDriveUnit(RobotMap.BACK_RIGHT_DRIVE_TALON_ID, RobotMap.BACK_RIGHT_ROTATE_TALON_ID);

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

		// cw/ccw rotational from joystick
		humanDrive(fwd, str, rot);

		SwerveDrive.updateDashboard();

//		frontLeft.setTurnMotorTargetEnc(IncTurnTargetCommand.val);
//		frontRight.setTurnMotorTargetEnc(IncTurnTargetCommand.val);
//		backLeft.setTurnMotorTargetEnc(IncTurnTargetCommand.val);
//		backRight.setTurnMotorTargetEnc(IncTurnTargetCommand.val);

		// debug only
		// InputOutputComm.putDouble(
		// InputOutputComm.LogTable.kMainLog, "ChillySwerve/FL_absAngle",
		// frontLeft.getAbsAngle());
		// InputOutputComm.putDouble(
		// InputOutputComm.LogTable.kMainLog, "ChillySwerve/FR_absAngle",
		// frontRight.getAbsAngle());
		// InputOutputComm.putDouble(
		// InputOutputComm.LogTable.kMainLog, "ChillySwerve/BL_absAngle",
		// backLeft.getAbsAngle());
		// InputOutputComm.putDouble(
		// InputOutputComm.LogTable.kMainLog, "ChillySwerve/BR_absAngle",
		// backRight.getAbsAngle());

		// logger.info(String.format("fla %.2f fra %.2f rla %.2f rra %.2f",
		// frontLeft.getAbsAngle(),
		// frontRight.getAbsAngle(), backLeft.getAbsAngle(),
		// backRight.getAbsAngle()));

//		 logger.info(String.format("flep(%.2f,%d) frep(%.2f,%d) rlep(%.2f,%d) rrep(%.2f,%d)",
//				 frontLeft.getTurnEncPos(),frontLeft.getTurnCLT(),
//				 frontRight.getTurnEncPos(), frontRight.getTurnCLT(), 
//				 backLeft.getTurnEncPos(),backLeft.getTurnCLT(),
//				 backRight.getTurnEncPos(),backRight.getTurnCLT()
//				 )
//				 );

		// logger.info("FL:" + frontLeft);
//		logger.info("FR:" + frontRight);
		// logger.info("BL:" + backLeft);
		// logger.info("BR:" + backRight);

		// logger.debug(String.format("fra :" + frontRight));

		// frontRight.setTurnPower(rot);
		// logger.info(String.format("pwp:%2.f",
		// frontRight.turnMotor.getSensorCollection().getPulseWidthPosition()));

		// frontLeft.setTurnPower(rot);
		// backLeft.setTurnPower(rot);
		// backRight.setTurnPower(rot);

		// double targetEnc = 0 * 1000;
		// frontLeft.turnMotor.set(ControlMode.Position, targetEnc);
		// frontRight.turnMotor.set(ControlMode.Position, targetEnc);
		// backLeft.turnMotor.set(ControlMode.Position, targetEnc);
		// backRight.turnMotor.set(ControlMode.Position, 1000);
		// ChillySwerve.setAllEncPos(0);

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

		double loc1 = angleToLoc(wa1);
		double loc2 = angleToLoc(wa2);
		double loc3 = angleToLoc(wa3);
		double loc4 = angleToLoc(wa4);

		SmartDashboard.putNumber("loc1", loc1);
		SmartDashboard.putNumber("loc2", loc2);
		SmartDashboard.putNumber("loc3", loc3);
		SmartDashboard.putNumber("loc4", loc4);

		setTargetEncPos(wa2, wa1, wa3, wa4);
		
//		setTargetEncPos(angleToLoc(wa2), angleToLoc(wa1), angleToLoc(wa3), angleToLoc(wa4));

//		SmartDashboard.putNumber("FL AbsAng", frontLeft.getAbsAngle());
//		SmartDashboard.putNumber("FR AbsAng", frontRight.getAbsAngle());
//		SmartDashboard.putNumber("BL AbsAng", backLeft.getAbsAngle());
//		SmartDashboard.putNumber("BR AbsAng", backRight.getAbsAngle());

//		setTargetEncPos(angleToEncPos(wa2), angleToEncPos(wa1), angleToEncPos(wa3), angleToEncPos(wa4));

//		logger.info(String.format("lwa1:%.2f, lwa2:%.2f, lwa3:%.2f, lwa4:%.2f, ", loc1, loc2, loc3, loc4));
	}

	private static double angleToLoc(double angle) {
		
		double mul = 0;
		
		if (angle < 0) {
			mul = .5d + ((180d - Math.abs(angle)) / 360d);
		} else {
			mul = angle / 360d;
		}
		
		return 360 * mul;
		
	}

	private static double angleToEncPos(double angle) {
		if (angle < 0) {
			angle = 360 + angle;
		}

		double encpos = angle * (1024 / 360.0);

		if (encpos < 0) {
			logger.error(String.format("Error : a:%.2f enc:%.2f", angle, encpos));
			throw new RuntimeException();
		}

		logger.info(String.format("encpos:%.2f", encpos));

		return encpos;
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

	public static void setTargetEncPos(double fl, double fr, double bl, double br) {
		logger.info("Location Set : " + String.format("fl %.2f fr %.2f bl %.2f br %.2f", fl, fr, bl, br));

		double flta = frontLeft.getTargetAngle(fl);
		double frta = frontRight.getTargetAngle(fr);
		double blta = backLeft.getTargetAngle(bl);
		double brta = backRight.getTargetAngle(br);

		SmartDashboard.putNumber("FL TA", flta);
		SmartDashboard.putNumber("FR TA", frta);
		SmartDashboard.putNumber("BL TA", blta);
		SmartDashboard.putNumber("BR TA", brta);

		frontLeft.setTurnMotorTargetEnc(flta);
		frontRight.setTurnMotorTargetEnc(frta);
		backLeft.setTurnMotorTargetEnc(blta);
		backRight.setTurnMotorTargetEnc(brta);

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

	public static void resetAllTurnEncodersToZero() {
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

	public static void setAllLocation(double loc) {
		setTargetEncPos(loc, loc, loc, loc);
	}

	public static void updateDashboard() {
		SmartDashboard.putNumber("FL Enc Pos", frontLeft.getTurnEncPos());
		SmartDashboard.putNumber("FR Enc Pos", frontRight.getTurnEncPos());
		SmartDashboard.putNumber("BL Enc Pos", backLeft.getTurnEncPos());
		SmartDashboard.putNumber("BR Enc Pos", backRight.getTurnEncPos());

		SmartDashboard.putNumber("FL CLT", frontLeft.getTurnCLT());
		SmartDashboard.putNumber("FR CLT", frontRight.getTurnCLT());
		SmartDashboard.putNumber("BL CLT", backLeft.getTurnCLT());
		SmartDashboard.putNumber("BR CLT", backRight.getTurnCLT());

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
