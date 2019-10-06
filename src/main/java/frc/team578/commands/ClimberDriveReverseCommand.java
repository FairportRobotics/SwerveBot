package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClimberDriveReverseCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClimberDriveReverseCommand.class);

    public ClimberDriveReverseCommand() {
        requires(Robot.climberSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing ClimberDriveReverseCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec ClimberDriveReverseCommand");
        Robot.climberSubsystem.climbWheelsBackwards();
    }

    @Override
    // Called when button is released
    protected void interrupted() {
        log.info("Interrupted ClimberDriveReverseCommand " + timeSinceInitialized());
        Robot.climberSubsystem.climbWheelsStop();
    }

    @Override
    // Will never happen
    protected void end() {
        log.info("Ending ClimberDriveReverseCommand " + timeSinceInitialized());
        Robot.climberSubsystem.climbWheelsStop();
    }

    // Will never happen
    @Override
    protected boolean isFinished() {
        return false;
    }
}
