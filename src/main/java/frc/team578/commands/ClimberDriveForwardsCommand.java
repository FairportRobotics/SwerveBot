package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClimberDriveForwardsCommand extends Command {

    private static final Logger log = LogManager.getLogger(ClimberDriveForwardsCommand.class);

    public ClimberDriveForwardsCommand() {
        requires(Robot.climberSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("Initializing ClimberDriveForwardsCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec ClimberDriveForwardsCommand");
        Robot.climberSubsystem.climbWheelsForward();
    }

    @Override
    // Called when button is released
    protected void interrupted() {
        log.info("Interrupted ClimberDriveForwardsCommand " + timeSinceInitialized());
        Robot.climberSubsystem.climbWheelsStop();
    }

    @Override
    // Will never happen
    protected void end() {
        log.info("Ending ClimberDriveForwardsCommand " + timeSinceInitialized());
        Robot.climberSubsystem.climbWheelsStop();
    }

    @Override
    // Will never happen
    protected boolean isFinished() {
        return false;
    }
}
