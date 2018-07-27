package team2102.lib.swerve;

import team2102.lib.SPIGyro;

public class OrientedSwerveDrive {

	private final SwerveDrive m_drive;

	private final SPIGyro m_gyro;

	private final double m_correctionRate;

	public OrientedSwerveDrive(final SwerveDrive drive, final SPIGyro gyro, final double correctionRate) {
		m_drive = drive;
		m_gyro = gyro;
		m_correctionRate = correctionRate;
	}

	public void drive(final double mag, final double dir, final double targetHeading) {
		final double heading = m_gyro.getYaw();

		final double absAngleRad = (dir - heading) / 180.0 * Math.PI;
		final double forward = mag * -Math.cos(absAngleRad);
		final double strafe = mag * -Math.sin(absAngleRad);

		final double correction = SPIGyro.fixRange(heading - targetHeading) * m_correctionRate;

		m_drive.drive(forward, strafe, correction);
	}
}
