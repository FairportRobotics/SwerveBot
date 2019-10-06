package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntakeRetractCommand extends Command {

    private static final Logger log = LogManager.getLogger(IntakeRetractCommand.class);

    public IntakeRetractCommand() {
        requires(Robot.cargoIntakeSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing IntakeRetractCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec IntakeRetractCommand");
        Robot.cargoIntakeSubsystem.retractIntake();
    }

    @Override
    protected void interrupted() { log.info("Interrupted IntakeRetractCommand"); }

    @Override
    protected void end() {
        log.info("Ending IntakeRetractCommand " + timeSinceInitialized());
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = true; // Robot.cargoIntakeSubsystem.isIntakeRetracted();
        log.info("IntakeRetractCommand is Finished : " + isFinished);
        return isFinished;
    }
}
