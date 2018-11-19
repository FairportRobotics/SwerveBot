package frc.team578.systems;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.Robot;
import frc.team578.robot.RobotMap;
import frc.team578.swerve.drive.CanTalonSwerveEnclosure;
import frc.team578.swerve.drive.SwerveDrive;
import frc.team578.swerve.math.CentricMode;

public class SwerveDriveSubsystem extends Subsystem {

	private static final SwerveDriveSubsystem sds = new SwerveDriveSubsystem();

	private WPI_TalonSRX frontLeftDriveTalon;
	private WPI_TalonSRX frontLeftSwerveTalon;
	private WPI_TalonSRX frontRightDriveTalon;
	private WPI_TalonSRX frontRightSwerveTalon;
	private WPI_TalonSRX backLeftDriveTalon;
	private WPI_TalonSRX backRightDriveTalon;
	private WPI_TalonSRX backLeftSwerveTalon;
	private WPI_TalonSRX backRightSwerveTalon;

	private CanTalonSwerveEnclosure frontLeft;
	private CanTalonSwerveEnclosure frontRight;
	private CanTalonSwerveEnclosure backLeft;
	private CanTalonSwerveEnclosure backRight;

	private SwerveDrive swerveDrive;

	// TODO : SET
	public final double GEAR_RATIO = (1988d / 1.2);
	private final double L = .625;
	private final double W = .47;

	// PIDF values - turn
	private final double turn_kP = 18;
	private final double turn_kI = 0.0;
	private final double turn_kD = 0.001;
	private final double turn_kF = 0.0;
	private final int turn_kIZone = 0;
	private final int TIMEOUT_MS = 0; // set to zero if skipping confirmation
	private final int PIDLOOP_IDX = 0; // set to zero if primary loop
	private final int PROFILE_SLOT = 0;
	private final boolean ALIGNED_TURN_SENSOR = false; // encoder polarity

	// joystick
	private double fwd = 0.0;
	private double str = 0.0;
	private double rot = 0.0;
	// gyro angle
	private double angleDeg = 0.0;

	// swerve inputs
	private final double JOYSTICK_DEADZONE = 0.1;

	private static boolean initialized = false;

	private SwerveDriveSubsystem() {
	}

	public static SwerveDriveSubsystem create() {

		if (!initialized) {

			sds.frontLeftDriveTalon = sds.configureDrive(RobotMap.FRONT_LEFT_DRIVE_TALON_ID,
					RobotMap.FRONT_LEFT_REVERSE_DRIVE);
			sds.frontRightDriveTalon = sds.configureDrive(RobotMap.FRONT_RIGHT_DRIVE_TALON_ID,
					RobotMap.FRONT_RIGHT_REVERSE_DRIVE);
			sds.backLeftDriveTalon = sds.configureDrive(RobotMap.BACK_LEFT_DRIVE_TALON_ID,
					RobotMap.BACK_LEFT_REVERSE_DRIVE);
			sds.backRightDriveTalon = sds.configureDrive(RobotMap.BACK_RIGHT_DRIVE_TALON_ID,
					RobotMap.BACK_RIGHT_REVERSE_DRIVE);

			sds.frontLeftSwerveTalon = sds.configureRotate(RobotMap.FRONT_LEFT_ROTATE_TALON_ID,
					RobotMap.FRONT_LEFT_REVERSE_TURN, sds.turn_kP, sds.turn_kI, sds.turn_kD, sds.turn_kF,
					sds.turn_kIZone);
			sds.frontRightSwerveTalon = sds.configureRotate(RobotMap.FRONT_RIGHT_ROTATE_TALON_ID,
					RobotMap.FRONT_RIGHT_REVERSE_TURN, sds.turn_kP, sds.turn_kI, sds.turn_kD, sds.turn_kF,
					sds.turn_kIZone);
			sds.backLeftSwerveTalon = sds.configureRotate(RobotMap.BACK_LEFT_ROTATE_TALON_ID,
					RobotMap.BACK_LEFT_REVERSE_TURN, sds.turn_kP, sds.turn_kI, sds.turn_kD, sds.turn_kF,
					sds.turn_kIZone);
			sds.backRightSwerveTalon = sds.configureRotate(RobotMap.BACK_RIGHT_ROTATE_TALON_ID,
					RobotMap.BACK_RIGHT_REVERSE_TURN, sds.turn_kP, sds.turn_kI, sds.turn_kD, sds.turn_kF,
					sds.turn_kIZone);

			sds.frontLeft = new CanTalonSwerveEnclosure("front left", sds.frontLeftDriveTalon, sds.frontLeftSwerveTalon,
					sds.GEAR_RATIO);
			sds.frontRight = new CanTalonSwerveEnclosure("front right", sds.frontRightDriveTalon,
					sds.frontRightSwerveTalon, sds.GEAR_RATIO);
			sds.backLeft = new CanTalonSwerveEnclosure("back left", sds.backLeftDriveTalon, sds.backLeftSwerveTalon,
					sds.GEAR_RATIO);
			sds.backRight = new CanTalonSwerveEnclosure("back right", sds.backRightDriveTalon, sds.backRightSwerveTalon,
					sds.GEAR_RATIO);

			sds.swerveDrive = new SwerveDrive(sds.frontLeft, sds.frontRight, sds.backLeft, sds.backRight, sds.W, sds.L);

			initialized = true;
		}

		return sds;
	}

	private WPI_TalonSRX configureDrive(int talonID, boolean revMotor) {
		WPI_TalonSRX _talon = new WPI_TalonSRX(talonID);
		_talon.setInverted(revMotor);
		_talon.configSelectedFeedbackSensor(FeedbackDevice.None, 0, 0);
		_talon.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen, 0);
		_talon.set(ControlMode.PercentOutput, 0);
		return _talon;
	}

	private WPI_TalonSRX configureRotate(int talonID, boolean revMotor, double pCoeff, double iCoeff, double dCoeff,
			double fCoeff, int iZone) {

		WPI_TalonSRX _talon = new WPI_TalonSRX(talonID);

		_talon.configSelectedFeedbackSensor(FeedbackDevice.Analog, PIDLOOP_IDX, TIMEOUT_MS);
		_talon.configSetParameter(ParamEnum.eFeedbackNotContinuous, 0, 0, 0, 0); // wrap the position (1023 -> 0)

		_talon.selectProfileSlot(PROFILE_SLOT, PIDLOOP_IDX);
		_talon.config_kP(PROFILE_SLOT, pCoeff, TIMEOUT_MS);
		_talon.config_kI(PROFILE_SLOT, iCoeff, TIMEOUT_MS);
		_talon.config_kD(PROFILE_SLOT, dCoeff, TIMEOUT_MS);
		_talon.config_kF(PROFILE_SLOT, fCoeff, TIMEOUT_MS);
		_talon.config_IntegralZone(PROFILE_SLOT, iZone, TIMEOUT_MS);

		_talon.configNominalOutputForward(0, TIMEOUT_MS);
		_talon.configNominalOutputReverse(0, TIMEOUT_MS);

		_talon.configPeakOutputForward(1, TIMEOUT_MS);
		_talon.configPeakOutputReverse(-1, TIMEOUT_MS);

		_talon.setInverted(revMotor);
		_talon.setSensorPhase(ALIGNED_TURN_SENSOR);

//		_talon.configPeakCurrentLimit(50, TIMEOUT_MS);
//		_talon.enableCurrentLimit(true);

		return _talon;
	}

	public void setTurnMotorTargetEnc(int encVal) {
		frontRight.setEncPosition(encVal);
		frontLeft.setEncPosition(encVal);
		backRight.setEncPosition(encVal);
		backLeft.setEncPosition(encVal);
	}

	public void teleopPeriodic() {
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
//		str *= -1;

		angleDeg = PigeonGyro.getAngle();

		swerveDrive.move(fwd, str, rot, angleDeg);
	}

	public void updateDashboard() {
		SmartDashboard.putNumber("FL Enc Pos", frontLeft.getEncPosition());
		SmartDashboard.putNumber("FR Enc Pos", frontRight.getEncPosition());
		SmartDashboard.putNumber("BL Enc Pos", backLeft.getEncPosition());
		SmartDashboard.putNumber("BR Enc Pos", backRight.getEncPosition());

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

		SmartDashboard.putString("FL", frontLeft.toString());
		SmartDashboard.putString("FR", frontRight.toString());
		SmartDashboard.putString("BL", backLeft.toString());
		SmartDashboard.putString("BR", backRight.toString());

	}

	@Override
	protected void initDefaultCommand() {

	}

	public void teleopInit() {

		// stop all drive & turn motors
		stopAll();
	}

	private void stopAll() {
		sds.frontLeft.stop();
		sds.frontRight.stop();
		sds.backLeft.stop();
		sds.backRight.stop();

	}

	private void setAllTurnPower(int i) {
	}

	private void setAllDrivePower(int i) {
		sds.frontLeft.setSpeed(i);
		sds.frontRight.setSpeed(i);
		sds.backLeft.setSpeed(i);
		sds.backRight.setSpeed(i);
	}

	public void disabledInit() {
	}

	public void disabledPeriodic() {
	}

	public void resetAllTurnEncoders() {
		sds.frontLeft.setEncPosition(0);
		sds.frontRight.setEncPosition(0);
		sds.backLeft.setEncPosition(0);
		sds.backRight.setEncPosition(0);

	}

	public void flipCentricMode() {
		if (swerveDrive.getCentricMode() == CentricMode.FIELD) {
			swerveDrive.setCentricMode(CentricMode.ROBOT);
		} else {
			swerveDrive.setCentricMode(CentricMode.FIELD);
		}
	}
}
