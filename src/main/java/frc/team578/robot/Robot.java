package frc.team578.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.swerve.IncTurnTargetCommand;
import frc.team578.swerve.SwerveDrive;
import frc.team578.systems.PigeonGyro;

public class Robot extends IterativeRobot {

	protected DriverStation ds;

	public static PowerDistributionPanel pdp;
	public static Joystick driveGamepad;
	public static JoystickButton bx;

	@Override
	public void robotInit() {
		// Initialize robot subsystems
		PigeonGyro.initialize();

		// Initialize Drive controller classes
		SwerveDrive.initialize();

		// retrieve Driver Station instance
		ds = DriverStation.getInstance();

		driveGamepad = new Joystick(RobotMap.CONTROL_GAMEPAD_ID);
		bx = new JoystickButton(driveGamepad, 1);
		bx.whenPressed(new IncTurnTargetCommand());

		pdp = new PowerDistributionPanel(0);
	}

	@Override
	public void autonomousInit() {
	}

	/** This function is called periodically during autonomous */
	@Override
	public void autonomousPeriodic() {
		// debug only (read position sensors)
		PigeonGyro.printStats();
	}

	@Override
	public void teleopInit() {
		SwerveDrive.teleopInit();
	}

	@Override
	public void teleopPeriodic() {
		SwerveDrive.teleopPeriodic();
		SmartDashboard.putData(pdp);
		Scheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		SwerveDrive.disabledInit();
	}

	@Override
	public void disabledPeriodic() {
		SwerveDrive.disabledPeriodic();
	}

	@Override
	public void testInit() {
		SwerveDrive.resetAllTurnEncoders();
	}

	@Override
	public void testPeriodic() {
		SwerveDrive.updateDashboard();
	}

//  private double getGyroAngle() {
//    // double gyroAngle = 0.0;
//    // double gyroAngle = NavXSensor.getYaw();  // -180 deg to +180 deg
////    double gyroAngle = NavXSensor.getAngle(); // continuous angle (can be larger than 360 deg)
//	  
//	  double gyroAngle = PigeonGyro.getAngle();
//
//    // System.out.println("getGyroAngle:  Gyro angle = " + gyroAngle);
//
//    // send output data for test & debug
//    // InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Connected",navX.isConnected());
//    // InputOutputComm.putBoolean(InputOutputComm.LogTable.kMainLog,"Auto/IMU_Calibrating",navX.isCalibrating());
//
//    // System.out.println("gyroAngle = " + gyroAngle);
//
//    return gyroAngle;
//  }
}
