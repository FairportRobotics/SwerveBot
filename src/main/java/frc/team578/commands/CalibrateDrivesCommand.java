package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.Robot;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;
import frc.team578.robot.utils.PIDFinished;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;
import java.util.function.Supplier;


public class CalibrateDrivesCommand extends TimedCommand implements UpdateDashboard {

    private static final Logger log = LogManager.getLogger(CalibrateDrivesCommand.class);

    static final int max_run_time_sec = 10;
    static final int stableCounts = 3;
    static final long checkIntervalMillis = 50;

    PIDFinished pidFinished;

    public CalibrateDrivesCommand() {
        super(max_run_time_sec);
        System.err.println("Constructor");

//        requires(Robot.swerveDriveSubsystem);
    }

    @Override
    protected void initialize() {
        System.err.println("Initializing CalibrateDrivesCommand");
        Supplier<Double> supplier = Robot.swerveDriveSubsystem::getSteerCLTErrorSum;
        Predicate<Double> successTest = (x) -> x < 20;

        pidFinished = new PIDFinished(checkIntervalMillis, stableCounts, supplier, successTest);

        Robot.swerveDriveSubsystem.calibrateAllSteerEncoders();

    }

    protected void execute() {
        System.err.println("Exec");
    }


    @Override
    protected void interrupted() {
        System.err.println("Interrupted CalibrateDrivesCommand");
//        Robot.swerveDriveSubsystem.stop();
    }


    @Override
    protected boolean isFinished() {

        boolean stableFound = pidFinished.isStable();
        boolean timeOutFound = isTimedOut();
        boolean isFinished = stableFound || timeOutFound;

        if (isFinished) {
            System.err.println("Calibration Finish Found");
            if (timeOutFound) {
                log.warn("CalibrateDrivesCommand timed out");
            }
            if (stableFound) {
                log.info("CalibrateDrivesCommand found stable");
            }
        }

        System.err.println("isFinished : " + isFinished);
        System.err.println("isFinished : " + pidFinished);
        return isFinished;

    }

    @Override
    protected void end() {
        System.err.println("Ending CalibrateDrivesCommand " + timeSinceInitialized());

        Robot.swerveDriveSubsystem.stop();
        Robot.swerveDriveSubsystem.zeroAllSteerEncoders();

//        if (!isTimedOut()) {
//            log.info("Zeroing Steer Encoders");
//            Robot.swerveDriveSubsystem.calibrateAllSteerEncoders();
//        }
//
//        Robot.swerveDriveSubsystem.stop();

    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putBoolean("calibc.timedout", isTimedOut());
        SmartDashboard.putNumber("calibc.serrderiv", Robot.swerveDriveSubsystem.getSteerErrorDerivitiveSum());
//        SmartDashboard.putBoolean("calibc.instopz", );

    }
}
