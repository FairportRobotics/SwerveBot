package frc.team1778.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team1778.ChillySwerve.ChillySwerve;
import frc.team1778.NetworkComm.InputOutputComm;
import frc.team1778.NetworkComm.RPIComm;
import frc.team1778.StateMachine.AutoStateMachine;
import frc.team1778.Systems.NavXSensor;

public class Robot extends IterativeRobot {

  protected DriverStation ds;
  protected AutoStateMachine autoSM;

  @Override
  public void robotInit() {
    // Initialize robot subsystems
    InputOutputComm.initialize();
    RPIComm.initialize();
    NavXSensor.initialize();

    // Initialize ChillySwerve Drive controller classes
    ChillySwerve.initialize();

    // Create Autonomous State Machine
    autoSM = new AutoStateMachine();

    // retrieve Driver Station instance
    ds = DriverStation.getInstance();

    InputOutputComm.putString(InputOutputComm.LogTable.kMainLog, "MainLog", "robot initialized...");
  }

  @Override
  public void autonomousInit() {
    InputOutputComm.putString(InputOutputComm.LogTable.kMainLog, "MainLog", "autonomous mode...");

    // start the auto state machine
    autoSM.start();
  }

  /** This function is called periodically during autonomous */
  @Override
  public void autonomousPeriodic() {

    autoSM.process();

    // debug only (read position sensors)
    getGyroAngle();
  }

  @Override
  public void teleopInit() {
    InputOutputComm.putString(InputOutputComm.LogTable.kMainLog, "MainLog", "teleop mode...");

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

    autoSM.stop();
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
    double gyroAngle = NavXSensor.getAngle(); // continuous angle (can be larger than 360 deg)

    // System.out.println("getGyroAngle:  Gyro angle = " + gyroAngle);

    // send output data for test & debug
    // InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Connected",navX.isConnected());
    // InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Calibrating",navX.isCalibrating());

    // System.out.println("gyroAngle = " + gyroAngle);
    InputOutputComm.putDouble(InputOutputComm.LogTable.kMainLog, "Auto/GyroAngle", gyroAngle);

    return gyroAngle;
  }
}
