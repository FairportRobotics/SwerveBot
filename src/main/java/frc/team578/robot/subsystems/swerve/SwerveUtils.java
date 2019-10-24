package frc.team578.robot.subsystems.swerve;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * A Utilities class that contains some useful methods
 */
public class SwerveUtils {

    /**
     * Convert encoder value from whatever the encoder returns to the -0.5 -
     * +0.5 range expected by the math library The encoder is assumed to be
     * continuous in the sense that it keeps counting beyond one turn and can go
     * to negative values as well
     *
     * @param encoderValue the value returned by the encoder
     * @return the encoder value in the -0.5 to +0.5 range
     */
    public static double convertEncoderValue(double encoderValue) {
        double encPos = encoderValue;
        // Reverse
//		encPos *= -1;
        // Make the scale of 1 rotation to be from 0 to 1 (1 being 1 rotation)
        // if that makes sense (it can be any whole number, with the number
        // corresponding to how many rotations it has gone through)
        encPos /= SwerveConstants.MAX_ENC_VAL;
        // Take the mod of that number, so it displays only a number from 0 to 1
        // (inclusive, exclusive)
        encPos = encPos % 1;

        return encPos;
    }

    /**
     * Convert the -0.5 - +0.5 range (for -180 to +180 degrees) to the value
     * expected by the encoder by using the encoder's gear ratio.
     *
     * @param angle     the angle to convert (in -0.5 to +0.5 range)
     * @param gearRatio the ratio of values in a single turn
     * @return the encoder value in the encoder units
     */
    public static double convertAngle(double angle, double gearRatio) {
        double encVal = angle;

        // Make the scale of 1 rotation to be from 0 to 1 (1 being 1 rotation)
        // if that makes sense (it can be any whole number, with the number
        // corresponding to how many rotations it has gone through)
        encVal *= gearRatio;

        return encVal;
    }

    public static WPI_TalonSRX createDriveTalon(int talonID, boolean revMotor) {
        WPI_TalonSRX talon = new WPI_TalonSRX(talonID);
        talon.configFactoryDefault();
        talon.setInverted(revMotor);
//        talon.configSelectedFeedbackSensor(FeedbackDevice.None, 0, 0);
        talon.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen, SwerveConstants.TIMEOUT_MS);
        talon.set(ControlMode.PercentOutput, 0);
        return talon;
    }

    public static WPI_TalonSRX createSteerTalon(int talonID, boolean revMotor, double pCoeff, double iCoeff, double dCoeff,
                                                 double fCoeff, int iZone) {

        WPI_TalonSRX talon = new WPI_TalonSRX(talonID);
        talon.configFactoryDefault();

        talon.configSelectedFeedbackSensor(FeedbackDevice.Analog, SwerveConstants.PIDLOOP_IDX, SwerveConstants.TIMEOUT_MS);
        talon.configSetParameter(ParamEnum.eFeedbackNotContinuous, 0, 0, 0, SwerveConstants.TIMEOUT_MS); // wrap the position (1023 -> 0)

        talon.selectProfileSlot(SwerveConstants.PROFILE_SLOT, SwerveConstants.PIDLOOP_IDX);
        talon.config_kP(SwerveConstants.PROFILE_SLOT, pCoeff, SwerveConstants.TIMEOUT_MS);
        talon.config_kI(SwerveConstants.PROFILE_SLOT, iCoeff, SwerveConstants.TIMEOUT_MS);
        talon.config_kD(SwerveConstants.PROFILE_SLOT, dCoeff, SwerveConstants.TIMEOUT_MS);
        talon.config_kF(SwerveConstants.PROFILE_SLOT, fCoeff, SwerveConstants.TIMEOUT_MS);
        talon.config_IntegralZone(SwerveConstants.PROFILE_SLOT, iZone, SwerveConstants.TIMEOUT_MS);

        talon.configNominalOutputForward(0, SwerveConstants.TIMEOUT_MS);
        talon.configNominalOutputReverse(0, SwerveConstants.TIMEOUT_MS);

        talon.configPeakOutputForward(1, SwerveConstants.TIMEOUT_MS);
        talon.configPeakOutputReverse(-1, SwerveConstants.TIMEOUT_MS);

        talon.setInverted(revMotor);

        talon.setSensorPhase(SwerveConstants.ALIGNED_TURN_SENSOR);


//		_talon.configPeakCurrentLimit(50, TIMEOUT_MS);
//		_talon.enableCurrentLimit(true);

        return talon;
    }
}
