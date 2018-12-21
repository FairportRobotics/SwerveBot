package frc.team578.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team578.robot.commands.FlipDriveModeCommand;
import frc.team578.robot.commands.IncTurnTargetCommand;
import frc.team578.systems.PigeonGyro;
import frc.team578.systems.SwerveDriveSubsystem;

public class Robot extends IterativeRobot {

	protected DriverStation ds;

//	public static PowerDistributionPanel pdp;
	public static Joystick driveGamepad;
	public static JoystickButton bx;
	public static JoystickButton ba;
	public static JoystickButton bb;
	public static JoystickButton by;

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

		by = new JoystickButton(driveGamepad, RobotMap.Y);
		by.whenPressed(new FlipDriveModeCommand());
		
//		pdp = new PowerDistributionPanel(0);
	}

	@Override
	public void autonomousInit() {
	}

	/** This function is called periodically during autonomous */
	@Override
	public void autonomousPeriodic() {
		PigeonGyro.updateDashboard();
		sds.updateDashboard();
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
		
		PigeonGyro.updateDashboard();
		
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
}
