package frc.team1778.StateMachine;

import edu.wpi.first.wpilibj.Utility;
import frc.team1778.NetworkComm.InputOutputComm;
import frc.team1778.Systems.NavXSensor;
import java.util.prefs.Preferences;

// event triggered when closed-loop gyro gets to a certain predetermined angle
public class ClosedLoopAngleEvent extends Event {

  private String name;

  private double targetAngleDeg = 0.0;
  private double errorDeg = 5.0;
  private double durationSec = 0.0;

  private long startTimeUs = 0;

  private InputOutputComm ioComm;
  private NavXSensor navX;

  public ClosedLoopAngleEvent(double targetAngleDeg, double errorDeg, double durationSec) {
    this.name = "<Gyro Angle Event>";

    this.targetAngleDeg = targetAngleDeg;
    this.errorDeg = errorDeg;
    this.durationSec = durationSec;

    NavXSensor.initialize();
    InputOutputComm.initialize();
  }

  // overloaded initialize method
  public void initialize() {
    // System.out.println("GyroAngleEvent initialized!");

    startTimeUs = Utility.getFPGATime();

    super.initialize();
  }

  private double getGyroAngle() {

    double gyroAngle = NavXSensor.getAngle(); // continuous angle (can be larger than 360 deg)

    // send output data for test & debug
    InputOutputComm.putBoolean(
        InputOutputComm.LogTable.kMainLog, "Auto/IMU_Connected", navX.isConnected());
    InputOutputComm.putBoolean(
        InputOutputComm.LogTable.kMainLog, "Auto/IMU_Calibrating", navX.isCalibrating());

    // System.out.println("gyroAngle = " + gyroAngle);
    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "Auto/GyroAngle", gyroAngle);

    return gyroAngle;
  }

  // overloaded trigger method
  public boolean isTriggered() {
    double gyroAngle = getGyroAngle();

    if (Math.abs(gyroAngle - targetAngleDeg) > errorDeg) {

      // outside error range...
      // reset timer and return false
      startTimeUs = Utility.getFPGATime();
      return false;
    }

    long currentTimeUs = Utility.getFPGATime();
    double delta = (currentTimeUs - startTimeUs) / 1e6;
    // System.out.println("delta = " + delta + " duration = " + durationSec);

    if (delta < durationSec) {
      // within error range, but not for enough time
      return false;
    }

    System.out.println("ClosedLoopAngleEvent triggered!");
    return true;
  }

  public void persistWrite(int counter, Preferences prefs) {

    // create node for event
    Preferences eventPrefs = prefs.node(counter + "_" + this.name);

    // store event details
    eventPrefs.put("class", this.getClass().toString());
  }
}
