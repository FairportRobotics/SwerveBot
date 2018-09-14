package team578.lib;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

public class SPIGyro {

	private final SPI m_port;

	private double m_yawZero = 0;
	private double m_pitchZero = 0;
	private double m_rollZero = 0;

	public SPIGyro(Port port) {
		m_port = new SPI(port);

		m_port.setChipSelectActiveLow();
	}

	public double getYaw() {
		return fixRange(getYawRaw() - m_yawZero);
	}

	public double getPitch() {
		return fixRange(getPitchRaw() - m_pitchZero);
	}

	public double getRoll() {
		return fixRange(getRollRaw() - m_rollZero);
	}

	public void resetYaw(double start) {
		m_yawZero = getYawRaw() + start;
	}

	public void resetPitch(double start) {
		m_pitchZero = getPitchRaw() + start;
	}

	public void resetRoll(double start) {
		m_rollZero = getRollRaw() + start;
	}

	private double getYawRaw() {
		return getAngle((byte) 1);
	}

	private double getPitchRaw() {
		return getAngle((byte) 2);
	}

	private double getRollRaw() {
		return getAngle((byte) 3);
	}

	// TODO: Combine the buffers?
	private double getAngle(byte idx) {
		byte[] cmd1 = new byte[] { idx };
		byte[] cmd2 = new byte[] { 0 };
		byte[] cmd3 = new byte[] { 0 };
		byte[] cmd4 = new byte[] { 0 };

		byte[] ans1 = new byte[1];
		byte[] ans2 = new byte[1];
		byte[] ans3 = new byte[1];
		byte[] ans4 = new byte[1];

		m_port.transaction(cmd1, ans1, 1);
		m_port.transaction(cmd2, ans2, 1);
		m_port.transaction(cmd3, ans3, 1);
		m_port.transaction(cmd4, ans4, 1);

		return (ans3[0] & 0xff | (ans4[0] << 8)) / 100.0;
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
