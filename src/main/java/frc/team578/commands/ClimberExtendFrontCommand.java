package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClimberExtendFrontCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClimberExtendFrontCommand.class);

    public ClimberExtendFrontCommand() {
        requires(Robot.climberSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing ClimberExtendFrontCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec ClimberExtendFrontCommand");
        Robot.climberSubsystem.extendFrontClimber();
    }

    @Override
    protected void interrupted() { log.info("Interrupted ClimberExtendFrontCommand"); }

    @Override
    protected void end() {
        log.info("Ending ClimberExtendFrontCommand " + timeSinceInitialized());
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = true; // Robot.climberSubsystem.isFrontClimberExtended();
        log.info("ClimberExtendFrontCommand is Finished : " + isFinished);
        return isFinished;
    }
}
