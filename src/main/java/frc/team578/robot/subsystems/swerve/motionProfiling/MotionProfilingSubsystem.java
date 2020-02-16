package frc.team578.robot.subsystems.swerve.motionProfiling;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import frc.team578.robot.Robot;
import frc.team578.robot.subsystems.interfaces.Initializable;

public class MotionProfilingSubsystem extends Subsystem implements Initializable {
    Translation2d m_frontLeftLocation = new Translation2d(0.2794, .3048);
    Translation2d m_frontRightLocation = new Translation2d(0.2794, -0.3048);
    Translation2d m_backLeftLocation = new Translation2d(-0.2794, 0.3048);
    Translation2d m_backRightLocation = new Translation2d(-0.2794, -0.3048);
    SwerveDriveOdometry m_odometry;
    Pose2d m_pose;

    public void initialize() {
        // Creating my kinematics object using the module locations
        SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
                m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
        );

        // Creating my odometry object from the kinematics object. Here,
        // our starting pose is 5 meters along the long end of the field and in the
        // center of the field along the short end, facing forward.
        m_odometry = new SwerveDriveOdometry(m_kinematics,
                Rotation2d.fromDegrees(-Robot.gyroSubsystem.getHeading()), new Pose2d(5.0, 13.5, new Rotation2d()));


    }

    public void periodic() {
        // Get my gyro angle. We are negating the value because gyros return positive
        // values as the robot turns clockwise. This is not standard convention that is
        // used by the WPILib classes.
        var gyroAngle = Rotation2d.fromDegrees(-(Robot.gyroSubsystem.getHeading()));
        SwerveModuleState flState = Robot.swerveDriveSubsystem.swerveDrive.swerveEnclosureFL.getState();
        SwerveModuleState frState = Robot.swerveDriveSubsystem.swerveDrive.swerveEnclosureFL.getState();
        SwerveModuleState blState = Robot.swerveDriveSubsystem.swerveDrive.swerveEnclosureFL.getState();
        SwerveModuleState brState = Robot.swerveDriveSubsystem.swerveDrive.swerveEnclosureFL.getState();
        // Update the pose
        m_pose = m_odometry.update(gyroAngle, flState, frState,
                blState, brState);
    }

    public void initDefaultCommand() {
    }
}