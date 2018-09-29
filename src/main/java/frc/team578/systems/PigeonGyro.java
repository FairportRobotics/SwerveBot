package frc.team578.systems;

import com.ctre.phoenix.sensors.PigeonIMU;

public class PigeonGyro {

	// https://www.ctr-electronics.com/downloads/pdf/Pigeon%20IMU%20User's%20Guide.pdf
	// https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/master/Java/PigeonStraight/src/org/usfirst/frc/team217/robot/Robot.java
	// http://www.ctr-electronics.com/downloads/api/java/html/index.html

	private static PigeonIMU _pigeon;

	private static double m_yawZero = 0;
	private static double m_pitchZero = 0;
	private static double m_rollZero = 0;

//	public PigeonGyro(Port port) {

//		double[] ypr = new double[3];
//		_pigeon.getYawPitchRoll(ypr);
//		System.out.println("Yaw:" + ypr[0]);

	// _pigeon.SetYaw(newAngle);
	// _pigeon.SetFusedHeading(newAngle)
	// _pigeon.enterCalibrationMode(CalibrationMode.Temperature,1);

	/* grab some input data from Pigeon and gamepad */
//		_pigeon.getGeneralStatus(genStatus);
//		_pigeon.getRawGyro(xyz_dps);
//		_pigeon.getFusedHeading(fusionStatus);

//	}

	private static boolean initialized = false;

	public static void initialize() {

		if (initialized)
			return;

		System.out.println("Pigeon initialize called...");

		try {
			_pigeon = new PigeonIMU(5);
		} catch (RuntimeException e) {
			System.err.println("Error : " + e.getMessage());
			throw e;
		}

		reset();

		initialized = true;
	}

	public static void reset() {
		System.err.println("Reset Pigeon");

		if (_pigeon != null) {

			final int kTimeoutMs = 30;
			_pigeon.setFusedHeading(0.0, kTimeoutMs);

			printStats();
		}
	}

	public static void printStats() {
//		PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
//		PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
//		double[] xyz_dps = new double[3];
		double currentAngle = _pigeon.getFusedHeading();
//		boolean angleIsGood = (_pigeon.getState() == PigeonIMU.PigeonState.Ready) ? true : false;
//		double currentAngularRate = xyz_dps[2];
//		double _targetAngle = 0;

//		System.out.println("error: " + (_targetAngle - currentAngle));
		System.out.println("gyro angle: " + currentAngle);
//		System.out.println("rate: " + currentAngularRate);
//		System.out.println("noMotionBiasCount: " + genStatus.noMotionBiasCount);
//		System.out.println("tempCompensationCount: " + genStatus.tempCompensationCount);
//		System.out.println(angleIsGood ? "Angle is good" : "Angle is NOT GOOD");
	}
	
	public static double getAngle() {
		PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
		double currentAngle = fusionStatus.heading;
		return currentAngle;
	}

//	public static double getYaw() {
//		return fixRange(getYawRaw() - m_yawZero);
//	}
//
//	public static void resetYaw(double start) {
//		m_yawZero = getYawRaw() + start;
//	}
//
//	private static double getYawRaw() {
//		return 0;
//	}
//
//	public static double fixRange(double angle) {
//		if (angle < -180) {
//			angle += 360;
//		} else if (angle > 180) {
//			angle -= 360;
//		}
//
//		return angle;
//	}
}
