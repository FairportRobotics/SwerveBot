package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SampleCommand extends Command {

    private static final Logger log = LogManager.getLogger(SampleCommand.class);

    public SampleCommand() {
        log.info("SampleCommand Constructor");
    }

    @Override
    protected void initialize() {
        log.info("Initializing SampleCommand");
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

        boolean isFinished = false;
        log.info ("SampleCommand is Finished : " + isFinished);
        return isFinished;
    }

    @Override
    protected void end() {
        log.info("Ending SampleCommand " + timeSinceInitialized());
    }
}
