package frc.team578.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.commands.IncTurnTargetCommand;
//import frc.team578.swerve.old.SwerveDriveOld;
//import frc.team578.swerve.old.IncTurnTargetCommand;
//import frc.team578.swerve.old.SwerveDrive;
import frc.team578.systems.PigeonGyro;
import frc.team578.systems.SwerveDriveSubsystem;

public class Robot extends IterativeRobot {

	protected DriverStation ds;

	public static PowerDistributionPanel pdp;
	public static Joystick driveGamepad;
	public static JoystickButton bx;
	public static JoystickButton ba;
	public static JoystickButton bb;

	public static SwerveDriveSubsystem sds;

	@Override
	public void robotInit() {
		// Initialize robot subsystems

		// Gyro
		PigeonGyro.initialize();

		// Initialize Drive controller classes
		sds = SwerveDriveSubsystem.create();

		// retrieve Driver Station instance
		ds = DriverStation.getInstance();

		driveGamepad = new Joystick(RobotMap.CONTROL_GAMEPAD_ID);

		bx = new JoystickButton(driveGamepad, RobotMap.X);
		bx.whenPressed(new IncTurnTargetCommand());

		ba = new JoystickButton(driveGamepad, RobotMap.A);
		ba.whenPressed(new IncTurnTargetCommand(1000));

		bb = new JoystickButton(driveGamepad, RobotMap.B);
		bb.whenPressed(new IncTurnTargetCommand(-100));

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
		PigeonGyro.reset();
		sds.teleopInit();
	}

	@Override
	public void teleopPeriodic() {
		sds.teleopPeriodic();
		sds.updateDashboard();
		
		SmartDashboard.putNumber("GyroAngle", PigeonGyro.getAngle());
		
		Scheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		sds.disabledInit();
	}

	@Override
	public void disabledPeriodic() {
		sds.disabledPeriodic();
	}

	@Override
	public void testInit() {
		sds.resetAllTurnEncoders();
	}

	@Override
	public void testPeriodic() {
		sds.updateDashboard();
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
