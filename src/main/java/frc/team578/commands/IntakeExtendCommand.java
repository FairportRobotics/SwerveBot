package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntakeExtendCommand extends Command {

    private static final Logger log = LogManager.getLogger(IntakeExtendCommand.class);

    public IntakeExtendCommand() {
        requires(Robot.cargoIntakeSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing IntakeExtendCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec MoveArmPIDCommand");
        Robot.cargoIntakeSubsystem.extendIntake();
    }

    @Override
    protected void interrupted() { log.info("Interrupted IntakeExtendCommand"); }

    @Override
    protected void end() {
        log.info("Ending IntakeExtendCommand " + timeSinceInitialized());
    }

    @Override
    protected boolean isFinished() {
        boolean isFinished = true; //Robot.cargoIntakeSubsystem.isIntakeExtended();
        log.info("IntakeExtendCommand is Finished : " + isFinished);
        return isFinished;
    }
}
