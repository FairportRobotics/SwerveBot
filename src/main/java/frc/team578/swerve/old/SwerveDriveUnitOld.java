package frc.team578.swerve.old;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class SwerveDriveUnitOld {
	
//	private static final Logger logger = LogManager.getLogger(SwerveDriveUnitOld.class);
//
//	private String name;
//	
//	// turn motor controller
//	public WPI_TalonSRX turnMotor;
//
//	// drive motor controller
//	private WPI_TalonSRX driveMotor;
//
//	// talon constants
//	private static final int TIMEOUT_MS = 0; // set to zero if skipping confirmation
//	private static final int PIDLOOP_IDX = 0; // set to zero if primary loop
//	private static final int PROFILE_SLOT = 0;
//
//	public static final boolean ALIGNED_TURN_SENSOR = false; // encoder polarity
//
//
//	// PIDF values - turn
//	private static final double turn_kP = 18;
//	private static final double turn_kI = 0.0;
//	private static final double turn_kD = 0.001;
//	private static final double turn_kF = 0.0;
//	private static final int turn_kIZone = 0;
//
//	public SwerveDriveUnitOld(String name, int driveTalonID, int turnTalonID, boolean reverseDrive, boolean reverseTurn) {
//
//		driveMotor = configureDrive(driveTalonID, reverseDrive);
//
//		turnMotor = configureRotate(turnTalonID, reverseTurn, turn_kP, turn_kI, turn_kD, turn_kF, turn_kIZone);
//		
//		this.name = name;
//
//		initialize();
//	}
//	
//	
//	private WPI_TalonSRX configureDrive(int talonID, boolean revMotor) {
//		WPI_TalonSRX _talon = new WPI_TalonSRX(talonID);
//		_talon.setInverted(revMotor);
//		_talon.configSelectedFeedbackSensor(FeedbackDevice.None,0,0);
//		_talon.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen, 0);
//		_talon.set(ControlMode.PercentOutput, 0);
//		return _talon;
//	}
//	
//
//	private WPI_TalonSRX configureRotate(int talonID, boolean revMotor, double pCoeff, double iCoeff, double dCoeff,
//			double fCoeff, int iZone) {
//		
//		WPI_TalonSRX _talon = new WPI_TalonSRX(talonID);
//		
//		
//
//		_talon.configSelectedFeedbackSensor(FeedbackDevice.Analog, PIDLOOP_IDX, TIMEOUT_MS);
//		_talon.configSetParameter(ParamEnum.eFeedbackNotContinuous, 0, 0, 0, 0); // wrap the position (1023 -> 0)
//		
//		
//		_talon.selectProfileSlot(PROFILE_SLOT, PIDLOOP_IDX);
//		_talon.config_kP(PROFILE_SLOT, pCoeff, TIMEOUT_MS);
//		_talon.config_kI(PROFILE_SLOT, iCoeff, TIMEOUT_MS);
//		_talon.config_kD(PROFILE_SLOT, dCoeff, TIMEOUT_MS);
//		_talon.config_kF(PROFILE_SLOT, fCoeff, TIMEOUT_MS);
//		_talon.config_IntegralZone(PROFILE_SLOT, iZone, TIMEOUT_MS);
//		
//		_talon.configNominalOutputForward(0, TIMEOUT_MS);
//		_talon.configNominalOutputReverse(0, TIMEOUT_MS);
//		
//		_talon.configPeakOutputForward(1, TIMEOUT_MS);
//		_talon.configPeakOutputReverse(-1, TIMEOUT_MS);
//		
//		_talon.setInverted(revMotor);
//		_talon.setSensorPhase(ALIGNED_TURN_SENSOR);
//
////		_talon.configPeakCurrentLimit(50, TIMEOUT_MS);
////		_talon.enableCurrentLimit(true);
//
//		return _talon;
//	}
//
//	public void stopMotors() {
//		setDrivePower(0);
//		setTurnPower(0);
//	}
//
//	public void initialize() {
//		stopMotors();
//	}
//
//	public double getTurnEncPos() {
//		return turnMotor.getSelectedSensorPosition(0);
//	}
//	
////	public double getTurnAngle() {
////		return (turnMotor.getSensorCollection().getAnalogIn() / 5.0) * 360.0;
////	}
//
//	public double getAbsAngle() {
//		// returns absolute angle of wheel in degrees (may wrap beyond 360 deg)
//		return (double) turnMotor.getSensorCollection().getAnalogIn() * (360.0 / 1024.0);
//	}
//	
//	public int getTurnMotorAnalogIn() {
//		return turnMotor.getSensorCollection().getAnalogIn();
//	}
//	
//	public int getTurnMotorAnalogInRaw() {
//		return turnMotor.getSensorCollection().getAnalogInRaw();
//	}
//	
//	public int getTurnCLT(int id) {
//		return turnMotor.getClosedLoopTarget(id);
//	}
//	
//	public int getTurnCLTError(int id) {
//		return turnMotor.getClosedLoopError(id);
//	}
//	
//	public double calculateTargetEncoderPos(double targetAngle) {
//		return 1024 - (targetAngle * (1024.0/360.0));
//	}
//	
//	public static double calculateBetterTargetEncoderPos(double currentPos, double targetAngle) {
//		double targetPos = targetAngle * (1024.0/360.0);
//		double cwDistanceFromTarget = (Math.abs((currentPos - 1024) % 1024) + targetPos) % 1024;
//		double ccwDistanceFromTarget = (1024 - cwDistanceFromTarget) % 1024;
//		
//		System.err.println(targetPos);
//		System.err.println(cwDistanceFromTarget);
//		System.err.println(ccwDistanceFromTarget);
//		
//		boolean turnCW = false;
//		double shortestDistance = 0;
//		if (cwDistanceFromTarget <= ccwDistanceFromTarget) {
//			turnCW = true;
//			shortestDistance = cwDistanceFromTarget;
//		} else {
//			turnCW = false;
//			shortestDistance = ccwDistanceFromTarget;
//		}
//		
//		return shortestDistance;
//	}
//	
//	public static void main(String[] args) {
//		double sd = calculateBetterTargetEncoderPos(1023, 0);
//		System.err.println(sd);
//	}
//
//	public double calculateFancyTargetEncoderPos(double targetAngle) {
//		
//		double taTemp = targetAngle % 360;
//
//		double currentAngle = getAbsAngle();
//		double currentAngleMod = currentAngle % 360;
//		if (currentAngleMod < 0)
//			currentAngleMod += 360;
//
//		double delta = currentAngleMod - taTemp;
//
//		if (delta > 180) {
//			taTemp += 360;
//		} else if (delta < -180) {
//			taTemp -= 360;
//		}
//
//		delta = currentAngleMod - taTemp;
//		if (delta > 90 || delta < -90) {
//			if (delta > 90)
//				taTemp += 180;
//			else if (delta < -90)
//				taTemp -= 180;
//			driveMotor.setInverted(false);
//		} else {
//			driveMotor.setInverted(true);
//		}
//
//		taTemp += currentAngle - currentAngleMod;
//
//		double encPos = taTemp * (1024.0 / 360.0);
//		
//		return encPos;
//	}
//
//	public void stopBoth() {
//		setDrivePower(0);
//		setTurnPower(0);
//	}
//
//	public void stopDrive() {
//		setDrivePower(0);
//	}
//	
//	// Will tell the talon to make current swerve position be 0
//	// Use to to calibrate wheel position with the talon encoder
//	public void resetTurnEnc() {
//		turnMotor.setSelectedSensorPosition(0, PIDLOOP_IDX, TIMEOUT_MS);
//	}
//
//	public void setDrivePower(double percentVal) {
//		driveMotor.set(ControlMode.PercentOutput, percentVal);
//	}
//	
//	public void setTurnMotorTargetEnc(double target) {
//		turnMotor.set(ControlMode.Position, target);
//	}
//	
//	public void setTurnPower(double percentVal) {
//		turnMotor.set(ControlMode.PercentOutput, percentVal);
//	}
//
//	public void setBrakeMode(boolean b) {
//		if (b == true)
//			driveMotor.setNeutralMode(NeutralMode.Brake);
//		else
//			driveMotor.setNeutralMode(NeutralMode.Coast);
//	}
//	
//	public String getName() {
//		return name;
//	}
//	
//	@Override
//	public String toString() {
//		return String.format("enc:%.2f aang:%.2f ain:%d ainr:%d clt:%d ssp:%d",getTurnEncPos(), getAbsAngle(), 
//				getTurnMotorAnalogIn(), getTurnMotorAnalogInRaw(), getTurnCLT(0), turnMotor.getSelectedSensorPosition(0) ); 
//	}
}