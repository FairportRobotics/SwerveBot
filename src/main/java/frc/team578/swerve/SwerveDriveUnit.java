package frc.team578.swerve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class SwerveDriveUnit {
	
	private static final Logger logger = LogManager.getLogger(SwerveDriveUnit.class);

	private static String name;
	
	// turn motor controller
	public WPI_TalonSRX turnMotor;

	// drive motor controller
	private WPI_TalonSRX driveMotor;

	// talon constants
	private static final int TIMEOUT_MS = 0; // set to zero if skipping confirmation
	private static final int PIDLOOP_IDX = 0; // set to zero if primary loop
	private static final int PROFILE_SLOT = 0;

	public static final boolean REVERSE_DRIVE_MOTOR = false; // motor polarity

	public static final boolean REVERSE_TURN_MOTOR = false; // motor polarity
	public static final boolean ALIGNED_TURN_SENSOR = false; // encoder polarity

	// encoder variables
//	private final double ENCODER_PULSES_PER_REV = 20 * 4; 


	// PIDF values - turn
	private static final double turn_kP = 19;
	private static final double turn_kI = 0.0;
	private static final double turn_kD = 0.1;
	private static final double turn_kF = 0.0;
	private static final int turn_kIZone = 18;

	public SwerveDriveUnit(String name, int driveTalonID, int turnTalonID) {

		driveMotor = configureDrive(driveTalonID, REVERSE_DRIVE_MOTOR);

		turnMotor = configureRotate(turnTalonID, REVERSE_TURN_MOTOR, turn_kP, turn_kI, turn_kD, turn_kF, turn_kIZone);
		
		this.name = name;

		initialize();
	}
	
	
	private WPI_TalonSRX configureDrive(int talonID, boolean revMotor) {
		WPI_TalonSRX _talon = new WPI_TalonSRX(talonID);
		_talon.setInverted(revMotor);
		_talon.configSelectedFeedbackSensor(FeedbackDevice.None,0,0);
		_talon.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen, 0);
		_talon.set(ControlMode.PercentOutput, 0);
		return _talon;
	}
	

	private WPI_TalonSRX configureRotate(int talonID, boolean revMotor, double pCoeff, double iCoeff, double dCoeff,
			double fCoeff, int iZone) {
		
		WPI_TalonSRX _talon = new WPI_TalonSRX(talonID);
		_talon.setInverted(revMotor);

		_talon.configSelectedFeedbackSensor(FeedbackDevice.Analog, PIDLOOP_IDX, TIMEOUT_MS);
//		 _talon.configSetParameter(ParamEnum.eFeedbackNotContinuous, 1, 0, 0, 0); // wrap the position (1023 -> 0)
		
		_talon.setSensorPhase(ALIGNED_TURN_SENSOR);
		_talon.selectProfileSlot(PROFILE_SLOT, PIDLOOP_IDX);
		_talon.config_kP(PROFILE_SLOT, pCoeff, TIMEOUT_MS);
		_talon.config_kI(PROFILE_SLOT, iCoeff, TIMEOUT_MS);
		_talon.config_kD(PROFILE_SLOT, dCoeff, TIMEOUT_MS);
		_talon.config_kF(PROFILE_SLOT, fCoeff, TIMEOUT_MS);
		_talon.config_IntegralZone(PROFILE_SLOT, iZone, TIMEOUT_MS);
		
		_talon.configNominalOutputForward(0, TIMEOUT_MS);
		_talon.configNominalOutputReverse(0, TIMEOUT_MS);
		
		_talon.configPeakOutputForward(.5, TIMEOUT_MS);
		_talon.configPeakOutputReverse(-.5, TIMEOUT_MS);

		_talon.configPeakCurrentLimit(50, TIMEOUT_MS);
		_talon.enableCurrentLimit(true);

		return _talon;
	}

	public void stopMotors() {
		setDrivePower(0);
		setTurnPower(0);
	}

	public void initialize() {
		stopMotors();
	}

	public double getTurnEncPos() {
		return turnMotor.getSelectedSensorPosition(0);
	}
	
	public double getTurnAngle() {
		return (turnMotor.getSensorCollection().getAnalogIn() / 5.0) * 360.0;
	}

	public double getAbsAngle() {
		// returns absolute angle of wheel in degrees (may wrap beyond 360 deg)
		return (double) turnMotor.getSensorCollection().getAnalogIn() * (360.0 / 1024.0);
	}
	
	public int getTurnMotorAnalogIn() {
		return turnMotor.getSensorCollection().getAnalogIn();
	}
	
	public int getTurnCLT(int id) {
		return turnMotor.getClosedLoopTarget(id);
	}
	
	public int getTurnCLTError(int id) {
		return turnMotor.getClosedLoopError(id);
	}
	
	public double calculateTargetEncoderPos(double targetAngle) {
		return targetAngle * (1024.0/360.0);
	}

	public double calculateFancyTargetEncoderPos(double targetAngle) {
		
		double taTemp = targetAngle % 360;

		double currentAngle = getAbsAngle();
		double currentAngleMod = currentAngle % 360;
		if (currentAngleMod < 0)
			currentAngleMod += 360;

		double delta = currentAngleMod - taTemp;

		if (delta > 180) {
			taTemp += 360;
		} else if (delta < -180) {
			taTemp -= 360;
		}

		delta = currentAngleMod - taTemp;
		if (delta > 90 || delta < -90) {
			if (delta > 90)
				taTemp += 180;
			else if (delta < -90)
				taTemp -= 180;
			driveMotor.setInverted(false);
		} else {
			driveMotor.setInverted(true);
		}

		taTemp += currentAngle - currentAngleMod;

		double encPos = taTemp * (1024.0 / 360.0);
		
		return encPos;
	}

	public void stopBoth() {
		setDrivePower(0);
		setTurnPower(0);
	}

	public void stopDrive() {
		setDrivePower(0);
	}
	
	public void resetTurnEnc() {
		turnMotor.setSelectedSensorPosition(0, PIDLOOP_IDX, TIMEOUT_MS);
	}

	public void setDrivePower(double percentVal) {
		driveMotor.set(ControlMode.PercentOutput, percentVal);
	}
	
	public void setTurnMotorTargetEnc(double target) {
		turnMotor.set(ControlMode.Position, target);
	}
	
	public void setTurnPower(double percentVal) {
		turnMotor.set(ControlMode.PercentOutput, percentVal);
	}

	public void setBrakeMode(boolean b) {
		if (b == true)
			driveMotor.setNeutralMode(NeutralMode.Brake);
		else
			driveMotor.setNeutralMode(NeutralMode.Coast);
	}
	
	@Override
	public String toString() {
		return String.format("enc:%.2f aang:%.2f ain:%d clt:%d ssp:%d",getTurnEncPos(), getAbsAngle(), 
				getTurnMotorAnalogIn(), getTurnCLT(0), turnMotor.getSelectedSensorPosition(0) ); 
	}
}