package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClimberRetractFrontCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClimberRetractFrontCommand.class);

    public ClimberRetractFrontCommand() {
        requires(Robot.climberSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing ClimberRetractFrontCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec ClimberRetractFrontCommand");
        Robot.climberSubsystem.retractFrontClimber();
    }

    @Override
    protected void interrupted() { log.info("Interrupted ClimberRetractFrontCommand"); }

    @Override
    protected void end() {
        log.info("Ending ClimberRetractFrontCommand " + timeSinceInitialized());
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = true; //Robot.climberSubsystem.isFrontClimberRetracted();
        log.info("ClimberRetractFrontCommand is Finished : " + isFinished);
        return isFinished;
    }
}
