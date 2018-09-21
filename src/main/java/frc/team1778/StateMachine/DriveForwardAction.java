package frc.team1778.StateMachine;

import frc.team1778.ChillySwerve.ChillySwerve;
import frc.team1778.NetworkComm.InputOutputComm;
import frc.team1778.Systems.NavXSensor;

public class DriveForwardAction extends Action {

  private String name;
  private double speed = 0.0;
  private boolean resetGyro = true;
  private double headingDeg = 0.0;

  public DriveForwardAction(double speed, boolean resetGyro, double headingDeg) {
    this.name = "<Drive Forward Action>";
    this.speed = speed;
    this.resetGyro = resetGyro;
    this.headingDeg = headingDeg; // absolute heading to use if not resetting gyro

    ChillySwerve.initialize();
    NavXSensor.initialize();
    InputOutputComm.initialize();
  }

  public DriveForwardAction(String name, double speed, boolean resetGyro, double headingDeg) {
    this.name = name;
    this.speed = speed;
    this.resetGyro = resetGyro;
    this.headingDeg = headingDeg; // absolute heading to use if not resetting gyro

    ChillySwerve.initialize();
    NavXSensor.initialize();
    InputOutputComm.initialize();
  }

  private double getGyroAngle() {
    // double gyroAngle = 0.0;
    // double gyroAngle = NavXSensor.getYaw();  // -180 deg to +180 deg
    double gyroAngle = NavXSensor.getAngle(); // continuous angle (can be larger than 360 deg)

    // System.out.println("autoPeriodicStraight:  Gyro angle = " + gyroAngle);

    // send output data for test & debug
    InputOutputComm.putBoolean(
        InputOutputComm.LogTable.kMainLog, "Auto/IMU_Connected", NavXSensor.isConnected());
    InputOutputComm.putBoolean(
        InputOutputComm.LogTable.kMainLog, "Auto/IMU_Calibrating", NavXSensor.isCalibrating());

    // System.out.println("gyroAngle = " + gyroAngle);
    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "Auto/GyroAngle", gyroAngle);

    return gyroAngle;
  }

  // action entry
  public void initialize() {
    // do some drivey initialization

    ChillySwerve.autoInit(resetGyro, headingDeg, false);

    super.initialize();
  }

  // called periodically
  public void process() {

    // do some drivey stuff
    ChillySwerve.autoGyroStraight(speed, headingDeg);

    // get gyro angle
    // (not used for anything else here except reporting to driver)
    getGyroAngle();

    super.process();
  }

  // state cleanup and exit
  public void cleanup() {
    // do some drivey cleanup

    ChillySwerve.autoStop();

    // cleanup base class
    super.cleanup();
  }
}
