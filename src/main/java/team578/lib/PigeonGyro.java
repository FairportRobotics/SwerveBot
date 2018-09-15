package team578.lib;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU.CalibrationMode;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

public class PigeonGyro {
	
	// https://www.ctr-electronics.com/downloads/pdf/Pigeon%20IMU%20User's%20Guide.pdf
	// https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/master/Java/PigeonStraight/src/org/usfirst/frc/team217/robot/Robot.java
	// http://www.ctr-electronics.com/downloads/api/java/html/index.html
	
	PigeonIMU _pigeon = new PigeonIMU(0); /* example Pigeon with device ID 0 */
	
	private double m_yawZero = 0;
	private double m_pitchZero = 0;
	private double m_rollZero = 0;

	public PigeonGyro(Port port) {
		
		double [] ypr = new double[3];
		_pigeon.getYawPitchRoll(ypr);
		System.out.println("Yaw:" + ypr[0]);
		
		// _pigeon.SetYaw(newAngle);
		// _pigeon.SetFusedHeading(newAngle)
		// _pigeon.enterCalibrationMode(CalibrationMode.Temperature,1);
		
		PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
		PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
		double [] xyz_dps = new double [3];
		/* grab some input data from Pigeon and gamepad*/
		_pigeon.getGeneralStatus(genStatus);
		_pigeon.getRawGyro(xyz_dps);
		_pigeon.getFusedHeading(fusionStatus);
		double currentAngle = fusionStatus.heading;
		boolean angleIsGood = (_pigeon.getState() == PigeonIMU.PigeonState.Ready) ? true : false;
		double currentAngularRate = xyz_dps[2];
		double _targetAngle = 0;
		
		System.out.println("------------------------------------------");
		System.out.println("error: " + (_targetAngle - currentAngle) );
		System.out.println("angle: "+ currentAngle);
		System.out.println("rate: "+ currentAngularRate);
		System.out.println("noMotionBiasCount: "+ genStatus.noMotionBiasCount);
		System.out.println("tempCompensationCount: "+ genStatus.tempCompensationCount);
		System.out.println( angleIsGood ? "Angle is good" : "Angle is NOT GOOD");
		System.out.println("------------------------------------------");

	}

	public double getYaw() {
		return fixRange(getYawRaw() - m_yawZero);
	}


	public void resetYaw(double start) {
		m_yawZero = getYawRaw() + start;
	}

	private double getYawRaw() {
		return 0;
	}

	
	public static double fixRange(double angle) {
		if (angle < -180) {
			angle += 360;
		} else if (angle > 180) {
			angle -= 360;
		}

		return angle;
	}
}
