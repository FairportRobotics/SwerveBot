package team578.lib.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class SwerveModule {

	private static final double k_rotateDeadzoneMargin = 4;

	private final TalonSRX m_driveMotor, m_rotateMotor;

	private double m_driveZeroPosition = 0.0;
	private final double m_rotateZeroPosition;

	public SwerveModule(final TalonSRX driveMotor, final TalonSRX rotateMotor, final double zeroPosition) {
		m_rotateZeroPosition = zeroPosition;

		m_driveMotor = driveMotor;
		// m_driveMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		// m_driveMotor.changeControlMode(TalonControlMode.PercentVbus);
		// m_driveMotor.set(0.0);

		
		/*
		 * Will neeed PID / Analog POT for rotate
		 */
		m_rotateMotor = rotateMotor;
		// m_rotateMotor.setFeedbackDevice(FeedbackDevice.AnalogPot);
		// m_rotateMotor.changeControlMode(TalonControlMode.Position);
		// m_rotateMotor.setPID(5.0, 0.0, 0.0, 0.0, 0, 0.0, 0);
		// m_rotateMotor.set(m_rotateZeroPosition);
	}

	public void resetDrivePosition() {
		// m_driveZeroPosition = m_driveMotor.getPosition();
	}

	public double getDrivePosition() {
		// return m_driveMotor.getPosition() - m_driveZeroPosition;
		return 0;
	}

	public void setPower(final double power) {
		 m_driveMotor.set(ControlMode.PercentOutput,power);
	}

	public double getRotatePosition() {
		// return m_rotateMotor.getPosition();
		return 0;
	}

	public double getAngle() {
		return 0;
		// return (m_rotateMotor.getPosition() - m_rotateZeroPosition) / 1024.0
		// * 360.0;
	}

	public void setAngle(double targetAngle) {
		final double angleDiff = targetAngle - getAngle();
		if (angleDiff > 180.0) {
			targetAngle -= 360.0;
		} else if (angleDiff < -180.0) {
			targetAngle += 360.0;
		}

		double targetPosition = targetAngle / 360.0 * 1024.0 + m_rotateZeroPosition;
		// m_rotateMotor.set(targetPosition);
	}

	@Deprecated
	public double _getVelocity() {
		// return m_driveMotor.getEncVelocity();
		return 0;
	}
}
