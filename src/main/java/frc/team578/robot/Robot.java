package frc.team578.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.subsystems.*;
import frc.team578.robot.subsystems.swerve.motionProfiling.FieldPosition;
import frc.team578.robot.subsystems.swerve.motionProfiling.MotionProfiling;
import edu.wpi.first.wpilibj.command.Command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Robot extends TimedRobot {

    private static final Logger log = LogManager.getLogger(Robot.class);

    // Operator Interface
    public static OI oi;

    // Subsystems
    public static SwerveDriveSubsystem swerveDriveSubsystem;
    public static GyroSubsystem gyroSubsystem;
    public static UsbCamera camera;
    public static MotionProfiling motionProfiling;

    public static final boolean useSwerveDrive = true;
    public static boolean useMotionProfiling = false;


    @Override
    public void robotInit() {
        
        try {

            log.info("Starting Robot Init");

            gyroSubsystem = new GyroSubsystem("gyro");
            gyroSubsystem.initialize();
            log.info("Gyro Subsystem Initialized");

            swerveDriveSubsystem = new SwerveDriveSubsystem();
            swerveDriveSubsystem.initialize();
            

            motionProfiling = new MotionProfiling();
            //vectArray(0,0, 0,.5, .5,.5, .5,0, 0,0), 1000);
            
            log.info("Swerve Drive Subsystem Initialized");
            log.info("Motion Profiling Subsystem Initialized");

//            climberSubsystem = new ClimberSubsystem();
//            climberSubsystem.initialize();
//            log.info("Climber Subsystem Initialized");
//
//            armSubsystem = new ArmSubsystem();
//            armSubsystem.initialize();
//            log.info("Arm Subsystem Initialized");
//
//            cargoIntakeSubsystem = new CargoIntakeSubsystem();
//            cargoIntakeSubsystem.initialize();
//            log.info("Cargo Intake Subsystem Initialized");
//
//
//            elevatorSubsystem = new ElevatorSubsystem();
//            elevatorSubsystem.initialize();
//            log.info("Elevator Subsystem Initialized");

            camera = CameraServer.getInstance().startAutomaticCapture();
            // cam.setResolution(100, 75);
            // cam.setFPS(-1);
            log.info("Initialized Camera");

            oi = new OI();
            oi.initialize();
            log.info("OI Subsystem Initialized");

        } catch (Throwable t) {
            log.error("Error In Robot Initialization : " + t.getMessage(), t);
            throw t;
        }

        log.info("Robot Init Complete");
    }

    @Override
    public void robotPeriodic() {

        Scheduler.getInstance().run();
        
    }

    @Override
    public void disabledInit() {
        super.disabledInit();
    }

    @Override
    public void disabledPeriodic() {
        updateAllDashboards();
        Scheduler.getInstance().run();
    }


    @Override
    public void autonomousInit() {

        Robot.swerveDriveSubsystem.stop();

    }

    @Override
    public void autonomousPeriodic() {

        updateAllDashboards();
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        System.out.println("update teleopinit");
        useMotionProfiling = false;
        Robot.gyroSubsystem.reset();
        Robot.swerveDriveSubsystem.stop();
        FieldPosition.init();
    }

    @Override
    public void teleopPeriodic() {


        updateAllDashboards();
        Scheduler.getInstance().run();
        
        FieldPosition.periodic();
        if(useMotionProfiling){
            motionProfiling.periodic();
        }
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
    }


    public void updateAllDashboards() {
        Robot.swerveDriveSubsystem.updateDashboard();
        Robot.gyroSubsystem.updateDashboard();
//        Robot.climberSubsystem.updateDashboard();
        FieldPosition.updateDashboard();
        SmartDashboard.updateValues();;
    }

    private Vector2d[] vectArray(double... a){
        Vector2d[] out = new Vector2d[a.length / 2];
        for(int i = 0; i < out.length; i++)
            out[i] = new Vector2d(a[2*i], a[2*i+1]);
        return out;
    }
}
