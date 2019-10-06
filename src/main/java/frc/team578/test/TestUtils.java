package frc.team578.test;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.team578.subsystems.swerve.SwerveUtils;

public class TestUtils {
    public static WPI_TalonSRX createSteerTalon(String name, int talonID, boolean revMotor, double pCoeff, double iCoeff, double dCoeff,
                                                double fCoeff, int iZone) {

        WPI_TalonSRX talon = SwerveUtils.createSteerTalon(talonID, revMotor, pCoeff, iCoeff, dCoeff, fCoeff, iZone);
        talon.setName(name);
        return talon;
    }
}
