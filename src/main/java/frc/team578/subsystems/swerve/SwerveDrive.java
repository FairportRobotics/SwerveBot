package frc.team578.robot.subsystems.swerve;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.team578.robot.RobotMap;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;
import frc.team578.robot.subsystems.swerve.math.CentricMode;
import frc.team578.robot.subsystems.swerve.math.SwerveDirective;
import frc.team578.robot.subsystems.swerve.math.SwerveMath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The main class for the SwerveDrive subsystem: This class handles all aspects
 * of controlling the swerve drive.
 */
public class SwerveDrive implements UpdateDashboard {

    private static final Logger log = LogManager.getLogger(SwerveDrive.class);

    // Enclosures 1-4 are the drive/steer combos
    private final TalonSwerveEnclosure swerveEnclosureFR;
    private final TalonSwerveEnclosure swerveEnclosureFL;
    private final TalonSwerveEnclosure swerveEnclosureBL;
    private final TalonSwerveEnclosure swerveEnclosureBR;
    private final frc.team578.robot.subsystems.swerve.math.SwerveMath swerveMath;


    public SwerveDrive(TalonSwerveEnclosure swerveEnclosureFL, TalonSwerveEnclosure swerveEnclosureFR,
                       TalonSwerveEnclosure swerveEnclosureBL, TalonSwerveEnclosure swerveEnclosureBR, double width, double length) {

        this.swerveEnclosureFR = swerveEnclosureFR;
        this.swerveEnclosureFL = swerveEnclosureFL;
        this.swerveEnclosureBL = swerveEnclosureBL;
        this.swerveEnclosureBR = swerveEnclosureBR;

        // instantiate the swerve library with a gyro provider using pigeon1
        this.swerveMath = new SwerveMath(width, length);

        this.swerveEnclosureFR.orientSensor();
        this.swerveEnclosureFL.orientSensor();
        this.swerveEnclosureBL.orientSensor();
        this.swerveEnclosureBR.orientSensor();
    }

    /**
     * Change the centric-mode of the robot (this can be done dynamically any time
     * and will affect the robot behavior from that point on)
     */
//    public void setCentricMode(frc.team578.robot.subsystems.swerve.math.CentricMode centricMode) {
//        this.swerveMath.setCentricMode(centricMode);
//    }
//
//    public void setModeField() {
//        this.swerveMath.setModeField();
//    }
//
//    public CentricMode getCentricMode() {
//        return this.swerveMath.getCentricMode();
//    }

    /**
     * move Moves the robot based on 3 inputs - fwd (forward), str(strafe), and
     * rcw(rotation clockwise) Inputs are between -1 and 1, with 1 being full power,
     * -1 being full reverse, and 0 being neutral. The method uses gyro for field
     * centric driving, if it is enabled.
     *
     * @param fwd
     * @param str
     * @param rcw
     * @param gyroValue the value of the gyro input to be used by the calculation.
     *                  Optional. Only used when the robot is in field-centric mode.
     */
    public void move(double fwd, double str, double rcw, Double gyroValue) {
        // Get the move command calculated
        List<SwerveDirective> swerveDirectives = swerveMath.move(fwd, str, rcw, gyroValue);
        System.err.println("Move: " + fwd + "\nSteer: " + str + "\nRotate:" + rcw);
        //Reversed left and right for fun
        swerveEnclosureFL.move(swerveDirectives.get(0).getSpeed(), swerveDirectives.get(0).getAngle());
        swerveEnclosureFR.move(swerveDirectives.get(1).getSpeed(), swerveDirectives.get(1).getAngle());
        swerveEnclosureBR.move(swerveDirectives.get(2).getSpeed(), swerveDirectives.get(2).getAngle());
        swerveEnclosureBL.move(swerveDirectives.get(3).getSpeed(), swerveDirectives.get(3).getAngle());
    }

    public void stop() {
        swerveEnclosureFR.stopAllTalons();
        swerveEnclosureFL.stopAllTalons();
        swerveEnclosureBL.stopAllTalons();
        swerveEnclosureBR.stopAllTalons();
    }


    public void zeroAllSteerEncoders() {
        swerveEnclosureFR.stopAllTalons();
        swerveEnclosureFL.stopAllTalons();
        swerveEnclosureBL.stopAllTalons();
        swerveEnclosureBR.stopAllTalons();

        swerveEnclosureFR.zeroSteerEncoder();
        swerveEnclosureFL.zeroSteerEncoder();
        swerveEnclosureBL.zeroSteerEncoder();
        swerveEnclosureBR.zeroSteerEncoder();
    }

    /*
    Wherever the wheel is right now, move it to where we think true north should be for that wheel so we can see where north is.
     */
    public void moveSteerTrueNorth() {
        swerveEnclosureFL.moveSteerTrueNorth();
        swerveEnclosureFR.moveSteerTrueNorth();
        swerveEnclosureBL.moveSteerTrueNorth();
        swerveEnclosureBR.moveSteerTrueNorth();
    }


    public double getSteerCLTErrorSum() {
        return swerveEnclosureFL.getSteerCLTErrorAbs() + swerveEnclosureFR.getSteerCLTErrorAbs() + swerveEnclosureBL.getSteerCLTErrorAbs() + swerveEnclosureBR.getSteerCLTErrorAbs();
    }

    public double getSteerErrorDerivitiveSum() {
        return swerveEnclosureFL.getSteerErrorDerivitiveAbs() + swerveEnclosureFR.getSteerErrorDerivitiveAbs() + swerveEnclosureBL.getSteerErrorDerivitiveAbs() + swerveEnclosureBR.getSteerErrorDerivitiveAbs();
    }

    public void calibrateAllSteerEncoders() {
        swerveEnclosureFR.stopAllTalons();
        swerveEnclosureFL.stopAllTalons();
        swerveEnclosureBL.stopAllTalons();
        swerveEnclosureBR.stopAllTalons();

        swerveEnclosureFL.setSensorToAnalogPos();
        swerveEnclosureFR.setSensorToAnalogPos();
        swerveEnclosureBR.setSensorToAnalogPos();
        swerveEnclosureBL.setSensorToAnalogPos();

//        swerveEnclosureFR.setSelectedSensorPosition(-fl_talon.getSensorCollection().getAnalogIn());
//        fr_talon.setSelectedSensorPosition(-fr_talon.getSensorCollection().getAnalogIn());
//        bl_talon.setSelectedSensorPosition(-bl_talon.getSensorCollection().getAnalogIn());
//        br_talon.setSelectedSensorPosition(-br_talon.getSensorCollection().getAnalogIn());

        int flpos = SwerveConstants.FRONT_LEFT_TRUE_NORTH_ENC_POS;
        int frpos = SwerveConstants.FRONT_RIGHT_TRUE_NORTH_ENC_POS;
        int blpos = SwerveConstants.BACK_LEFT_TRUE_NORTH_ENC_POS;
        int brpos = SwerveConstants.BACK_RIGHT_TRUE_NORTH_ENC_POS;

        /* update motor controller */
        swerveEnclosureFL.moveSteerToEncoderPosition(flpos);
        swerveEnclosureFR.moveSteerToEncoderPosition(frpos);
        swerveEnclosureBL.moveSteerToEncoderPosition(blpos);
        swerveEnclosureBR.moveSteerToEncoderPosition(brpos);

    }


    public static SwerveDrive create() {

        WPI_TalonSRX frontLeftDriveTalon;
        WPI_TalonSRX frontLeftSwerveTalon;
        WPI_TalonSRX frontRightDriveTalon;
        WPI_TalonSRX frontRightSwerveTalon;
        WPI_TalonSRX backLeftDriveTalon;
        WPI_TalonSRX backRightDriveTalon;
        WPI_TalonSRX backLeftSwerveTalon;
        WPI_TalonSRX backRightSwerveTalon;

        frontLeftDriveTalon = SwerveUtils.createDriveTalon(RobotMap.FRONT_LEFT_DRIVE_TALON_ID,
                SwerveConstants.FRONT_LEFT_REVERSE_DRIVE);
        frontRightDriveTalon = SwerveUtils.createDriveTalon(RobotMap.FRONT_RIGHT_DRIVE_TALON_ID,
                SwerveConstants.FRONT_RIGHT_REVERSE_DRIVE);
        backLeftDriveTalon = SwerveUtils.createDriveTalon(RobotMap.BACK_LEFT_DRIVE_TALON_ID,
                SwerveConstants.BACK_LEFT_REVERSE_DRIVE);
        backRightDriveTalon = SwerveUtils.createDriveTalon(RobotMap.BACK_RIGHT_DRIVE_TALON_ID,
                SwerveConstants.BACK_RIGHT_REVERSE_DRIVE);

        frontLeftSwerveTalon = SwerveUtils.createSteerTalon(RobotMap.FRONT_LEFT_ROTATE_TALON_ID,
                SwerveConstants.FRONT_LEFT_REVERSE_TURN, SwerveConstants.turn_kP, SwerveConstants.turn_kI, SwerveConstants.turn_kD, SwerveConstants.turn_kF,
                SwerveConstants.turn_kIZone);
        frontRightSwerveTalon = SwerveUtils.createSteerTalon(RobotMap.FRONT_RIGHT_ROTATE_TALON_ID,
                SwerveConstants.FRONT_RIGHT_REVERSE_TURN, SwerveConstants.turn_kP, SwerveConstants.turn_kI, SwerveConstants.turn_kD, SwerveConstants.turn_kF,
                SwerveConstants.turn_kIZone);
        backLeftSwerveTalon = SwerveUtils.createSteerTalon(RobotMap.BACK_LEFT_ROTATE_TALON_ID,
                SwerveConstants.BACK_LEFT_REVERSE_TURN, SwerveConstants.turn_kP, SwerveConstants.turn_kI, SwerveConstants.turn_kD, SwerveConstants.turn_kF,
                SwerveConstants.turn_kIZone);
        backRightSwerveTalon = SwerveUtils.createSteerTalon(RobotMap.BACK_RIGHT_ROTATE_TALON_ID,
                SwerveConstants.BACK_RIGHT_REVERSE_TURN, SwerveConstants.turn_kP, SwerveConstants.turn_kI, SwerveConstants.turn_kD, SwerveConstants.turn_kF,
                SwerveConstants.turn_kIZone);

        TalonSwerveEnclosure frontLeft;
        TalonSwerveEnclosure frontRight;
        TalonSwerveEnclosure backLeft;
        TalonSwerveEnclosure backRight;

        frontLeft = new TalonSwerveEnclosure("fl", frontLeftDriveTalon, frontLeftSwerveTalon, SwerveConstants.FRONT_LEFT_TRUE_NORTH_ENC_POS);
        frontRight = new TalonSwerveEnclosure("fr", frontRightDriveTalon,
                frontRightSwerveTalon, SwerveConstants.FRONT_RIGHT_TRUE_NORTH_ENC_POS);
        backLeft = new TalonSwerveEnclosure("bl", backLeftDriveTalon, backLeftSwerveTalon, SwerveConstants.BACK_LEFT_TRUE_NORTH_ENC_POS);
        backRight = new TalonSwerveEnclosure("br", backRightDriveTalon, backRightSwerveTalon, SwerveConstants.BACK_RIGHT_TRUE_NORTH_ENC_POS);


        return new SwerveDrive(frontLeft, frontRight, backLeft, backRight, SwerveConstants.ROBOT_WIDTH, SwerveConstants.ROBOT_LENGTH);
    }

    @Override
    public void updateDashboard() {
        swerveEnclosureFL.updateDashboard();
        swerveEnclosureFR.updateDashboard();
        swerveEnclosureBL.updateDashboard();
        swerveEnclosureBR.updateDashboard();
    }


}
