package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClimberExtendRearCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClimberExtendRearCommand.class);

    public ClimberExtendRearCommand() {
        requires(Robot.climberSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing ClimberExtendRearCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec ClimberExtendRearCommand");
        Robot.climberSubsystem.extendRearClimber();
    }

    @Override
    protected void interrupted() { log.info("Interrupted ClimberExtendRearCommand"); }

    @Override
    protected void end() {
        log.info("Ending ClimberExtendRearCommand " + timeSinceInitialized());
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = true; //Robot.climberSubsystem.isRearClimberExtended();
        log.info("ClimberExtendRearCommand is Finished : " + isFinished);
        return isFinished;
    }
}
