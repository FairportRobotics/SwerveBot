package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import frc.team578.robot.subsystems.swerve.motionProfiling.FieldPosition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToggleUseMotionProfiling extends Command {

    private static final Logger log = LogManager.getLogger(SampleCommand.class);

    @Override
    protected void initialize() {
        Robot.useMotionProfiling = !Robot.useMotionProfiling;
        Robot.swerveDriveSubsystem.swerveDriveCommand.setProfilingPowerX(0);
        Robot.swerveDriveSubsystem.swerveDriveCommand.setProfilingPowerY(0);
        Robot.motionProfiling.restartTime();
        FieldPosition.resetBotPosition();
    }

    @Override
    protected void execute() {
        log.info("Exec SampleCommand");
    }


    @Override
    protected void interrupted() {
        log.info("Interrupted CalibrateDrivesCommand");
    }

    @Override
    protected boolean isFinished() {

        boolean isFinished = true;
        log.info ("SampleCommand is Finished : " + isFinished);
        return isFinished;
    }

    @Override
    protected void end() {
        log.info("Ending SampleCommand " + timeSinceInitialized());
    }
}
