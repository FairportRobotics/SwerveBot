package frc.team578.robot.test;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.subsystems.swerve.SwerveConstants;
import frc.team578.robot.utils.PIDFinished;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class SwerveSteerTest extends TimedRobot {

    private static final Logger log = LogManager.getLogger(SwerveSteerTest.class);

    public static final boolean REVERSE_TURN = true;

    public static final int TIMEOUT_MS = 0; // set to zero if skipping confirmation
    public static final int PIDLOOP_IDX = 0; // set to zero if primary loop
    public static final int PROFILE_SLOT = 0;
    public static final boolean ALIGNED_TURN_SENSOR = false; // encoder polarity
    public static final double turn_kP = 8;
    public static final double turn_kI = 0.0;
    public static final double turn_kD = 0.0;
    public static final double turn_kF = 0.0;
    public static final int turn_kIZone = 0;
    public static final double MAX_ENC_VAL = 1024;
    int target;
    boolean finished = false;


    WPI_TalonSRX fl_talon;
    WPI_TalonSRX fr_talon;
    WPI_TalonSRX bl_talon;
    WPI_TalonSRX br_talon;
    Joystick _joystick = new Joystick(0); /* make a joystick */
//    Faults _faults = new Faults(); /* temp to fill with latest faults */


    @Override
    public void robotInit() {




        fl_talon = TestUtils.createSteerTalon("t_fl",11,
                REVERSE_TURN, turn_kP, turn_kI, turn_kD, turn_kF,
                turn_kIZone);
        fr_talon = TestUtils.createSteerTalon("t_fr",12,
                REVERSE_TURN, turn_kP, turn_kI, turn_kD, turn_kF,
                turn_kIZone);
        bl_talon = TestUtils.createSteerTalon("t_bl",13,
                REVERSE_TURN, turn_kP, turn_kI, turn_kD, turn_kF,
                turn_kIZone);
        br_talon = TestUtils.createSteerTalon("t_br",14,
                REVERSE_TURN, turn_kP, turn_kI, turn_kD, turn_kF,
                turn_kIZone);

//        updateTarget();

        updateSD(fl_talon);
        updateSD(fr_talon);
        updateSD(bl_talon);
        updateSD(br_talon);
    }


    @Override
    public void autonomousInit() {

        // Supplier<Double> supplier = () -> Math.abs(fl_talon.getErrorDerivative()) + Math.abs(fr_talon.getErrorDerivative()) + Math.abs(bl_talon.getErrorDerivative()) + Math.abs(br_talon.getErrorDerivative());
        Supplier<Integer> supplier = () -> Math.abs(fl_talon.getClosedLoopError()) + Math.abs(fr_talon.getClosedLoopError()) + Math.abs(bl_talon.getClosedLoopError()) + Math.abs(br_talon.getClosedLoopError());
        Predicate<Integer> successTest = (x) -> x < 50;
        PIDFinished<Integer> pidFinished = new PIDFinished(checkIntervalMillis,stableCounts, supplier, successTest);

//        fl_talon.set(ControlMode.PercentOutput,0);
//        fr_talon.set(ControlMode.PercentOutput,0);
//        bl_talon.set(ControlMode.PercentOutput,0);
//        br_talon.set(ControlMode.PercentOutput,0);

        fl_talon.neutralOutput();
        fr_talon.neutralOutput();
        bl_talon.neutralOutput();
        br_talon.neutralOutput();



        fl_talon.setSelectedSensorPosition(-fl_talon.getSensorCollection().getAnalogIn());
        fr_talon.setSelectedSensorPosition(-fr_talon.getSensorCollection().getAnalogIn());
        bl_talon.setSelectedSensorPosition(-bl_talon.getSensorCollection().getAnalogIn());
        br_talon.setSelectedSensorPosition(-br_talon.getSensorCollection().getAnalogIn());

        int flpos = SwerveConstants.FRONT_LEFT_TRUE_NORTH_ENC_POS;
        int frpos = SwerveConstants.FRONT_RIGHT_TRUE_NORTH_ENC_POS;
        int blpos = SwerveConstants.BACK_LEFT_TRUE_NORTH_ENC_POS;
        int brpos = SwerveConstants.BACK_RIGHT_TRUE_NORTH_ENC_POS;


        /* update motor controller */
        fl_talon.set(ControlMode.Position, flpos);
        fr_talon.set(ControlMode.Position, frpos);
        bl_talon.set(ControlMode.Position, blpos);
        br_talon.set(ControlMode.Position, brpos);

        long start = System.currentTimeMillis();
        while (!pidFinished.isStable() || (System.currentTimeMillis() - start) > 5000) {
            System.err.println("Waiting");
            updateSD(fl_talon);
            updateSD(fr_talon);
            updateSD(bl_talon);
            updateSD(br_talon);
        }
        System.err.println("Done");

//        fl_talon.set(ControlMode.PercentOutput,0);
//        fr_talon.set(ControlMode.PercentOutput,0);
//        bl_talon.set(ControlMode.PercentOutput,0);
//        br_talon.set(ControlMode.PercentOutput,0);
//
//
//        fl_talon.setSelectedSensorPosition(0);
//        fr_talon.setSelectedSensorPosition(0);
//        bl_talon.setSelectedSensorPosition(0);
//        br_talon.setSelectedSensorPosition(0);
//
//        fl_talon.set(ControlMode.Position,0);
//        fr_talon.set(ControlMode.Position,0);
//        bl_talon.set(ControlMode.Position,0);
//        br_talon.set(ControlMode.Position,0);

        updateSD(fl_talon);
        updateSD(fr_talon);
        updateSD(bl_talon);
        updateSD(br_talon);
    }

    @Override
    public void teleopInit() {
        // TODO : Use These?
        // fl_talon.configAllowableClosedloopError();
        // fl_talon.neutralOutput();

        fl_talon.set(ControlMode.PercentOutput,0);
        fr_talon.set(ControlMode.PercentOutput,0);
        bl_talon.set(ControlMode.PercentOutput,0);
        br_talon.set(ControlMode.PercentOutput,0);


        fl_talon.setSelectedSensorPosition(0);
        fr_talon.setSelectedSensorPosition(0);
        bl_talon.setSelectedSensorPosition(0);
        br_talon.setSelectedSensorPosition(0);
    }



    @Override
    public void teleopPeriodic() {




        updateSD(fl_talon);
        updateSD(fr_talon);
        updateSD(bl_talon);
        updateSD(br_talon);


    }

    @Override
    public void disabledPeriodic() {
        updateSD(fl_talon);
        updateSD(fr_talon);
        updateSD(bl_talon);
        updateSD(br_talon);
    }

    @Override
    public void testInit() {
        fl_talon.set(ControlMode.Position,1024);
        fr_talon.set(ControlMode.Position,1024);
        bl_talon.set(ControlMode.Position,1024);
        br_talon.set(ControlMode.Position,1024);

    }

    @Override
    public void testPeriodic() {

        updateSD(fl_talon);
        updateSD(fr_talon);
        updateSD(bl_talon);
        updateSD(br_talon);
    }

    static int max_run_time_sec = 10;
    boolean found = false;
    int stableCounts = 2;
    double stopZone = 0;
    int successCount = 0;
    long lastChecked = 0;
    long checkIntervalMillis = 30;

    public boolean isTimedOut() {
        return false;
    }

    long lastUpdate = 0;
    public void updateSD(WPI_TalonSRX t) {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > 100) {
            lastUpdate = now;

            SmartDashboard.putData(t.getName() + "_Talon Data", t);
            SmartDashboard.putNumber(t.getName() + "_Sensor Vel:", t.getSelectedSensorVelocity());
            SmartDashboard.putNumber(t.getName() + "_Sensor Pos:", t.getSelectedSensorPosition());
            SmartDashboard.putNumber(t.getName() + "_CLT:", t.getClosedLoopTarget());
            SmartDashboard.putNumber(t.getName() + "_CLE:", t.getClosedLoopError());
            SmartDashboard.putNumber(t.getName() + "_Analog In", t.getSensorCollection().getAnalogIn());
            SmartDashboard.putNumber(t.getName() + "_Analog In Raw", t.getSensorCollection().getAnalogInRaw());
            SmartDashboard.putNumber(t.getName() + "_Out %", t.getMotorOutputPercent());
//        SmartDashboard.putBoolean(t.getName() + "_Any Faults:", _faults.hasAnyFault());
//        SmartDashboard.putBoolean(t.getName() + "_Out Of Phase:", _faults.SensorOutOfPhase);
            SmartDashboard.putNumber(t.getName() + "_errDeriv:", t.getErrorDerivative());
            SmartDashboard.putNumber(t.getName() + "_clerr", t.getClosedLoopError());
        }


    }
}

