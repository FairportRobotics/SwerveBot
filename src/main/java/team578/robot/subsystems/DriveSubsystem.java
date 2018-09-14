package team578.robot.subsystems;

import java.io.FileWriter;
import java.io.IOException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team578.lib.SPIGyro;
import team578.lib.swerve.OrientedSwerveDrive;
import team578.lib.swerve.SwerveDrive;
import team578.lib.swerve.SwerveModule;
import team578.robot.RobotMap;
import team578.robot.commands.TeleopDriveCommand;

public class DriveSubsystem extends Subsystem {

	private static final double k_correctionP = 1.0 / 180.0;
	private static final double k_correctionD = 0.0;

	private static final double k_wheelbaseLength = 0.625; // meters
	private static final double k_wheelbaseWidth = 0.47; // meters

	private static final double k_wheelDiameter = 0.1; // meters
	private static final int k_ticksPerRev = 2560;

	// TODO: Are these numbers right?
	// Max velocity: 1400 ticks / 100 ms (~1.63 m/s)
	// Max acceleration: 0.500 sec to top speed (~3.26 m/s/s)
	// Max jerk: ??? (maybe find second derivative of velocity graph)

	private static final double k_maxVelocity = 1.60;
	private static final double k_maxAccel = 3.20;

//	private static final Config k_config = new Config(FitMethod.HERMITE_CUBIC, Config.SAMPLES_HIGH, 1.0 / 50.0,
//			k_maxVelocity, k_maxAccel, 60.0);

	private final Preferences m_prefs = Preferences.getInstance();

	private final SPIGyro m_gyro = new SPIGyro(Port.kMXP);

	private final SwerveModule m_module1 = createModule(RobotMap.DRIVE_1, RobotMap.ROTATE_1, "Rotate 1");
	private final SwerveModule m_module2 = createModule(RobotMap.DRIVE_2, RobotMap.ROTATE_2, "Rotate 2");
	private final SwerveModule m_module3 = createModule(RobotMap.DRIVE_3, RobotMap.ROTATE_3, "Rotate 3");
	private final SwerveModule m_module4 = createModule(RobotMap.DRIVE_4, RobotMap.ROTATE_4, "Rotate 4");

	private final SwerveDrive m_drive = new SwerveDrive(m_module1, m_module2, m_module3, m_module4, k_wheelbaseLength,
			k_wheelbaseWidth);

	private final OrientedSwerveDrive m_orientedDrive = new OrientedSwerveDrive(m_drive, m_gyro, k_correctionP);

	public double m_targetHeading = 0.0;

//	private double m_prevHeadingDiff = 0.0;

//	private FileWriter _m_logFile;
//	private Timer _m_timer = new Timer();

	public DriveSubsystem() {
//		SmartDashboard.putData(this);
	}

	private SwerveModule createModule(final int driveId, final int rotateId, final String keyName) {
		final double failValue = -1.0;
		final double zeroPosition = m_prefs.getDouble(keyName, failValue);
		if (zeroPosition == failValue) {
			DriverStation.reportError("Failed to get zero position from preferences for " + keyName, false);
		}
		
		
		
		// Should we use this instead?
		WPI_TalonSRX drive = new WPI_TalonSRX(driveId);
		drive.setSafetyEnabled(false);
		drive.setExpiration(.25);
		drive.setNeutralMode(NeutralMode.Brake);
		drive.set(ControlMode.PercentOutput, 0);
		
		

		return new SwerveModule(drive, new TalonSRX(rotateId), zeroPosition);
	}

	
	/*
	 * This sets position of each rotation pot in the prefs.
	 */
	public void calibrateRotatePos() {
		DriverStation.reportWarning("Calibrating potentiometers", false);
		m_prefs.putDouble("Rotate 1", m_module1.getRotatePosition());
		m_prefs.putDouble("Rotate 2", m_module2.getRotatePosition());
		m_prefs.putDouble("Rotate 3", m_module3.getRotatePosition());
		m_prefs.putDouble("Rotate 4", m_module4.getRotatePosition());
	}

//	public void resetHeading() {
//		DriverStation.reportWarning("Resetting gyro heading", false);
//		m_gyro.resetYaw(0.0);
//	}
//
	public void drive(double mag, double dir) {
		System.err.println("Drive Mag " + mag + " dir " + dir);
		m_orientedDrive.drive(mag, dir, m_targetHeading);
	}
//
//	private void resetDistance() {
//		m_module1.resetDrivePosition();
//		m_module2.resetDrivePosition();
//		m_module3.resetDrivePosition();
//		m_module4.resetDrivePosition();
//	}

//	public EncoderFollower[] generateFollowers(final Waypoint[] points) {
//		final SwerveModifier modifier = new SwerveModifier(Pathfinder.generate(points, k_config))
//				.modify(k_wheelbaseWidth, k_wheelbaseLength, SwerveModifier.Mode.SWERVE_DEFAULT);
//		final EncoderFollower[] followers = new EncoderFollower[] {
//				new EncoderFollower(modifier.getFrontLeftTrajectory()),
//				new EncoderFollower(modifier.getFrontRightTrajectory()),
//				new EncoderFollower(modifier.getBackRightTrajectory()),
//				new EncoderFollower(modifier.getBackLeftTrajectory()) };
//
//		for (final EncoderFollower follower : followers) {
//			follower.configureEncoder(0, k_ticksPerRev, k_wheelDiameter);
//			follower.configurePIDVA(5.0, 0.0, 0.0, 1.0 / k_maxVelocity, 1.0 / k_maxAccel);
//		}
//
//		return followers;
//	}
//
//	public void startFollowing(final EncoderFollower[] followers) {
//		resetDistance();
//		for (final EncoderFollower follower : followers) {
//			follower.reset();
//		}
//	}
//
//	public boolean follow(final EncoderFollower[] followers) {
//		final double pos1 = m_module1.getDrivePosition();
//		final double pos2 = m_module2.getDrivePosition();
//		final double pos3 = m_module3.getDrivePosition();
//		final double pos4 = m_module4.getDrivePosition();
//
//		final SwerveCommands t_commands = new SwerveCommands();
//
//		t_commands.speed1 = followers[0].calculate((int) pos1);
//		t_commands.speed2 = followers[1].calculate((int) pos2);
//		t_commands.speed3 = followers[2].calculate((int) pos3);
//		t_commands.speed4 = followers[3].calculate((int) pos4);
//
//		final boolean finished = t_commands.speed1 == 0.0 && t_commands.speed2 == 0.0 && t_commands.speed3 == 0.0
//				&& t_commands.speed4 == 0.0;
//
//		t_commands.angle1 = Pathfinder.r2d(followers[0].getHeading());
//		t_commands.angle2 = Pathfinder.r2d(followers[1].getHeading());
//		t_commands.angle3 = Pathfinder.r2d(followers[2].getHeading());
//		t_commands.angle4 = Pathfinder.r2d(followers[3].getHeading());
//
//		final double headingDiff = SPIGyro.fixRange(m_gyro.getYaw() - m_targetHeading);
//		final double correction = headingDiff * k_correctionP + (headingDiff - m_prevHeadingDiff) * k_correctionD;
//		m_prevHeadingDiff = headingDiff;
//
//		final SwerveCommands c_commands = m_drive.calcModuleCommands(0.0, 0.0, correction);
//
//		m_drive.driveCommands(
//				SwerveCommandComponents.add(t_commands.toComponents(), c_commands.toComponents()).toCommands());
//
//		return finished;
//	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDriveCommand());
	}

}
