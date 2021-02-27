package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SwerveDriveCommand extends Command {

    private static final Logger log = LogManager.getLogger(SwerveDriveCommand.class);
    private double profilingPowerX = 0, profilingPowerY = 0, profilingPowerA = 0;
    private final double INPUT_FACTOR = 1;
    
    public SwerveDriveCommand() {
        requires(Robot.swerveDriveSubsystem);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {

        double fwd = INPUT_FACTOR*Robot.oi.gp1.getPadLeftY();

        // double str = Robot.oi.getStrafe();
        double str = INPUT_FACTOR*Robot.oi.gp1.getPadLeftX();

        double rot = INPUT_FACTOR*Robot.oi.gp1.getPadRightX();
        
        fwd += profilingPowerX;
        str += profilingPowerY;
        rot += profilingPowerA;

//      fwd *= -1;
//		str *= -1;
        if(fwd > 1)
            fwd = 1;
        if(fwd < -1)
            fwd = -1;
        if(str > 1)
            str = 1;
        if(str < -1)
            str = -1;
        double angleDeg = Robot.gyroSubsystem.getHeading();

        double mag = Math.sqrt(fwd*fwd + str*str);
        double ratio = 0;
        if(mag > .0001){
            ratio = deadband(mag)/mag;     //  deadband scalar for each component
        }
        
        if(Robot.useMotionProfiling)
            Robot.swerveDriveSubsystem.move(fwd, str, rot, angleDeg);
        else
            Robot.swerveDriveSubsystem.move(ratio*fwd, ratio*str, deadband(rot), angleDeg);

        SmartDashboard.putNumber("swrv.fwd", fwd);
        SmartDashboard.putNumber("swrv.str", str);
        SmartDashboard.putNumber("swrv.rot", rot);
        SmartDashboard.putNumber("swrv.angleDeg", angleDeg);

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        log.info("SwerveDriveCommand Ended");
    }

    @Override
    protected void interrupted() {
        log.info("SwerveDriveCommand Interrupted");

    }
    public void setProfilingPowerX(double x){
        profilingPowerX = x;
    }
    public void setProfilingPowerY(double y){
        profilingPowerY = y;
    }
    public void setProfilingPowerA(double a){
        profilingPowerA = a;
    }
    final double DEADBAND = 0.2;

    private double deadband(double value) {
        if (Math.abs(value) < DEADBAND) return 0.0;
        return (1/(1-DEADBAND) - DEADBAND/(1-DEADBAND)/Math.abs(value))*value;
    }

}
