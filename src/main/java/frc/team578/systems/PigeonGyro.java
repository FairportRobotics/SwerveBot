package frc.team578.systems;

import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.RobotMap;

public class PigeonGyro {

	// https://www.ctr-electronics.com/downloads/pdf/Pigeon%20IMU%20User's%20Guide.pdf
	// https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/master/Java/PigeonStraight/src/org/usfirst/frc/team217/robot/Robot.java
	// http://www.ctr-electronics.com/downloads/api/java/html/index.html

	private static PigeonIMU _pigeon;

	private static boolean initialized = false;

	public static void initialize() {

		if (initialized)
			return;

		System.out.println("Pigeon initialize");

		try {
			_pigeon = new PigeonIMU(RobotMap.PIGEON_IMU_ID);
		} catch (RuntimeException e) {
			System.err.println("Error : " + e.getMessage());
			throw e;
		}

		reset();

		initialized = true;
		
		PigeonGyro.updateDashboard();
	}

	public static void reset() {
		System.err.println("Reset Pigeon");

		if (_pigeon != null) {

			final int kTimeoutMs = 30;
			_pigeon.setFusedHeading(0.0, kTimeoutMs);
		}
	}

	public static void updateDashboard() {
		
		SmartDashboard.putNumber("pigeon getAngle", getAngle());
		SmartDashboard.putNumber("pigeon fusedHeading", _pigeon.getFusedHeading());
		SmartDashboard.putNumber("pigeon absCompHeading", _pigeon.getAbsoluteCompassHeading());
		SmartDashboard.putNumber("pigeon compHeading", _pigeon.getCompassHeading());
		SmartDashboard.putNumber("pigeon state", _pigeon.getState().value);
	}
	
	public static double getAngle() {
		return Math.abs(_pigeon.getFusedHeading() % 360);
		
//		return _pigeon.getAbsoluteCompassHeading();
		
	}
}
