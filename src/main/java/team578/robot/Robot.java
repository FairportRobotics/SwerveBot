package team578.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team578.robot.commands.CalibratePotCommand;
import team578.robot.commands.ResetGyroCommand;
import team578.robot.commands.TestAccelCommand;
import team578.robot.commands.TestRotationCommand;
import team578.robot.subsystems.DriveSubsystem;

public class Robot extends IterativeRobot {
	
	/*
	 * Framework taken from Team 2102
	 */

	public static OI oi;
	public static DriveSubsystem driveSubsystem;

	private Command m_autoCommand = null;
//	private final SendableChooser<Command> m_chooser = new SendableChooser<>();

	@Override
	public void robotInit() {
		oi = new OI();
		driveSubsystem = new DriveSubsystem();

//		final Waypoint[] points = new Waypoint[] { new Waypoint(0.0, 0.0, 0.0), new Waypoint(3.0, 0.0, 0.0),
//				new Waypoint(0.0, 0.0, 0.0),
				// new Waypoint(0.0, 0.0, 0.0),
				// new Waypoint(1.0, 1.0, 0.0),
				// new Waypoint(2.0, 0.0, 0.0),
				// new Waypoint(1.0, -1.0, 0.0),
				// new Waypoint(0.0, 0.0, 0.0),
//		};

//		m_chooser.addDefault("Do Nothing", null);
//		m_chooser.addObject("Test trajectory following", new FollowTrajectoryCommand(points));
//		m_chooser.addObject("Test acceleration", new TestAccelCommand());
//		m_chooser.addObject("Test rotation", new TestRotationCommand(0.0));
//		SmartDashboard.putData("Auto mode", m_chooser);

//		SmartDashboard.putData("Calibrate Potentiometers", new CalibratePotCommand());
//		SmartDashboard.putData("Reset Gyro", new ResetGyroCommand());
	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
//		driveSubsystem.resetHeading();
//
//		m_autoCommand = m_chooser.getSelected();
//
//		if (m_autoCommand != null) {
//			m_autoCommand.start();
//		}
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (m_autoCommand != null) {
			m_autoCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
//		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
//		LiveWindow.run();
	}
}
