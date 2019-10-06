package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClimberRetractRearCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClimberRetractRearCommand.class);

    public ClimberRetractRearCommand() {
        requires(Robot.climberSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing ClimberRetractRearCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec ClimberRetractRearCommand");
        Robot.climberSubsystem.retractRearClimber();
    }

    @Override
    protected void interrupted() { log.info("Interrupted ClimberRetractRearCommand"); }

    @Override
    protected void end() {
        log.info("Ending ClimberRetractRearCommand " + timeSinceInitialized());
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = true; //Robot.climberSubsystem.isRearClimberRetracted();
        log.info("ClimberRetractRearCommand is Finished : " + isFinished);
        return isFinished;
    }
}
