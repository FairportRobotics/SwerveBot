package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntakeSpinInwardCommand extends Command {

    private static final Logger log = LogManager.getLogger(IntakeRetractCommand.class);

    public IntakeSpinInwardCommand() {
        requires(Robot.cargoIntakeSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing IntakeSpinInwardCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec IntakeSpinInwardCommand");
        Robot.cargoIntakeSubsystem.spinIntakeInward();
    }

    @Override
    protected void interrupted() {
        log.info("Interrupted IntakeSpinInwardCommand");
        Robot.cargoIntakeSubsystem.spinIntakeStop();
    }

    @Override
    protected void end() {
        Robot.cargoIntakeSubsystem.spinIntakeStop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
