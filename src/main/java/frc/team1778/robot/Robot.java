package frc.team1778.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team1778.ChillySwerve.ChillySwerve;
import frc.team1778.Systems.NavXSensor;
import frc.team1778.Systems.PigeonGyro;

public class Robot extends IterativeRobot {

  protected DriverStation ds;

  @Override
  public void robotInit() {
    // Initialize robot subsystems
    NavXSensor.initialize();

    // Initialize ChillySwerve Drive controller classes
    ChillySwerve.initialize();

    // retrieve Driver Station instance
    ds = DriverStation.getInstance();

  }

  @Override
  public void autonomousInit() {
  }

  /** This function is called periodically during autonomous */
  @Override
  public void autonomousPeriodic() {
    // debug only (read position sensors)
    getGyroAngle();
  }

  @Override
  public void teleopInit() {

    ChillySwerve.teleopInit();
  }

  @Override
  public void teleopPeriodic() {
    // ChillySwerve-Drive command for all controllers
    ChillySwerve.teleopPeriodic();
  }

  @Override
  public void disabledInit() {

    ChillySwerve.disabledInit();
  }

  @Override
  public void disabledPeriodic() {
    ChillySwerve.disabledPeriodic();
  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  private double getGyroAngle() {
    // double gyroAngle = 0.0;
    // double gyroAngle = NavXSensor.getYaw();  // -180 deg to +180 deg
//    double gyroAngle = NavXSensor.getAngle(); // continuous angle (can be larger than 360 deg)
	  
	  double gyroAngle = PigeonGyro.getYaw();

    // System.out.println("getGyroAngle:  Gyro angle = " + gyroAngle);

    // send output data for test & debug
    // InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Connected",navX.isConnected());
    // InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Calibrating",navX.isCalibrating());

    // System.out.println("gyroAngle = " + gyroAngle);

    return gyroAngle;
  }
}
