package frc.team1778.ChillySwerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team1778.Systems.NavXSensor;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

public class ChillySwerveUnit {

  // turn motor controller
  private TalonSRX turnMotor;
  private int turnTalonID = 0;

  // drive motor controller
  private TalonSRX driveMotor;
  private int driveTalonID = 1;

  // talon constants
  private static final int TIMEOUT_MS = 0; // set to zero if skipping confirmation
  private static final int PIDLOOP_IDX = 0; // set to zero if primary loop
  private static final int PROFILE_SLOT = 0;

  public static final boolean REVERSE_DRIVE_MOTOR = false; // motor polarity
  public static final boolean ALIGNED_DRIVE_SENSOR = true; // encoder polarity

  public static final boolean REVERSE_TURN_MOTOR = false; // motor polarity
  public static final boolean ALIGNED_TURN_SENSOR = true; // encoder polarity

  // encoder variables
  private final double ENCODER_PULSES_PER_REV = 20 * 4; // am-3314a encoders
  private final double INCHES_PER_REV = (5.9 * 3.14159); // 5.9-in diameter wheel (worn)

  // PIDF values - drive
  private static final double drive_kP = 1.0;
  private static final double drive_kI = 0.0005;
  private static final double drive_kD = 0.0;
  private static final double drive_kF = 0.0;
  private static final int drive_kIZone = 18;

  // PIDF values - turn
  private static final double turn_kP = 1.0;
  private static final double turn_kI = 0.0;
  private static final double turn_kD = 0.0;
  private static final double turn_kF = 0.0;
  private static final int turn_kIZone = 18;

  public ChillySwerveUnit(int driveTalonID, int turnTalonID) {
    this.driveTalonID = driveTalonID;
    this.turnTalonID = turnTalonID;

    driveMotor =
        configureMotor(
            driveTalonID,
            REVERSE_DRIVE_MOTOR,
            drive_kP,
            drive_kI,
            drive_kD,
            drive_kF,
            drive_kIZone);
    driveMotor.configSelectedFeedbackSensor(
        FeedbackDevice.QuadEncoder, PIDLOOP_IDX, TIMEOUT_MS); // am-3314a quad encoder
    driveMotor.setSensorPhase(ALIGNED_DRIVE_SENSOR);
    driveMotor.setSelectedSensorPosition(0, PIDLOOP_IDX, TIMEOUT_MS);

    turnMotor =
        configureMotor(
            turnTalonID, REVERSE_TURN_MOTOR, turn_kP, turn_kI, turn_kD, turn_kF, turn_kIZone);
    turnMotor.configSelectedFeedbackSensor(
        FeedbackDevice.Analog, PIDLOOP_IDX, TIMEOUT_MS); // MA3 Absolute encoder
    turnMotor.setSensorPhase(ALIGNED_TURN_SENSOR);

    initialize();
  }

  // closed-loop motor configuration
  private TalonSRX configureMotor(
      int talonID,
      boolean revMotor,
      double pCoeff,
      double iCoeff,
      double dCoeff,
      double fCoeff,
      int iZone) {
    TalonSRX _talon;
    _talon = new TalonSRX(talonID);
    _talon.setInverted(revMotor);

    _talon.selectProfileSlot(PROFILE_SLOT, PIDLOOP_IDX);
    _talon.config_kP(PROFILE_SLOT, pCoeff, TIMEOUT_MS);
    _talon.config_kI(PROFILE_SLOT, iCoeff, TIMEOUT_MS);
    _talon.config_kD(PROFILE_SLOT, dCoeff, TIMEOUT_MS);
    _talon.config_kF(PROFILE_SLOT, fCoeff, TIMEOUT_MS);
    _talon.config_IntegralZone(PROFILE_SLOT, iZone, TIMEOUT_MS);

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

  public double getAbsAngle() {

    // returns absolute angle of wheel in degrees (may wrap beyond 360 deg)
    return (double) turnMotor.getSensorCollection().getAnalogIn() * (360.0 / 1024.0);
  }

  public void resetTurnEnc() {
    turnMotor.setSelectedSensorPosition(0, PIDLOOP_IDX, TIMEOUT_MS);
  }

  public void setEncPos(int d) {
    turnMotor.setSelectedSensorPosition(d, PIDLOOP_IDX, TIMEOUT_MS);
  }

  public int getTurnRotations() {
    return (int) (turnMotor.getSelectedSensorPosition(0) / ENCODER_PULSES_PER_REV);
  }

  public void setDrivePower(double percentVal) {
    driveMotor.set(ControlMode.PercentOutput, percentVal);
  }

  public void setTurnPower(double percentVal) {
    turnMotor.set(ControlMode.PercentOutput, percentVal);
  }

  public void setTargetAngle(double targetAngle) {

    targetAngle %= 360;

    double currentAngle = getAbsAngle();
    double currentAngleMod = currentAngle % 360;
    if (currentAngleMod < 0) currentAngleMod += 360;

    double delta = currentAngleMod - targetAngle;

    if (delta > 180) {
      targetAngle += 360;
    } else if (delta < -180) {
      targetAngle -= 360;
    }

    delta = currentAngleMod - targetAngle;
    if (delta > 90 || delta < -90) {
      if (delta > 90) targetAngle += 180;
      else if (delta < -90) targetAngle -= 180;
      driveMotor.setInverted(false);
    } else {
      driveMotor.setInverted(true);
    }

    targetAngle += currentAngle - currentAngleMod;

    targetAngle *= 1024.0 / 360.0;
    turnMotor.set(ControlMode.Position, targetAngle);
  }

  public void drivePath(EncoderFollower follower) {

    if (!follower.isFinished()) {

      Trajectory.Segment seg = follower.getSegment();
      double fakeDistance = seg.position;
      double speedx = follower.calculate((int) fakeDistance);
      double heading = follower.getHeading();

      // System.out.printf("%f\t%f\t%f\t%f\t%f\n",
      //		speed, speedx, dis, err, fakeDistance);
      setDrivePower(speedx);

      double gyroAngle = NavXSensor.getAngle();
      gyroAngle = Math.IEEEremainder(gyroAngle, 360);

      setTargetAngle(Pathfinder.r2d(heading) + gyroAngle);
    }
  }

  public void stopBoth() {
    setDrivePower(0);
    setTurnPower(0);
  }

  public void stopDrive() {
    setDrivePower(0);
  }

  public void setBrakeMode(boolean b) {
    if (b == true) driveMotor.setNeutralMode(NeutralMode.Brake);
    else driveMotor.setNeutralMode(NeutralMode.Coast);
  }
}
