package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import frc.team578.robot.enums.ElevatorPositionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveElevatorCommand extends Command {

    private static final Logger log = LogManager.getLogger(MoveElevatorCommand.class);

    ElevatorPositionEnum position;

    public MoveElevatorCommand(ElevatorPositionEnum position) {
        log.info("MoveElevatorCommand Constructor");
        this.position = position;
    }

    @Override
    protected void initialize() {
        log.info("Initializing MoveElevatorCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec MoveElevatorCommand");
        if (position == ElevatorPositionEnum.LEVEL_ONE) {
            Robot.elevatorSubsystem.moveToLevel(ElevatorPositionEnum.LEVEL_ONE);
        }
    }

    @Override
    protected void interrupted() {
        log.info("Interrupted MoveElevatorCommand");
    }

    @Override
    protected boolean isFinished() {

        boolean isFinished = true; // Robot.elevatorSubsystem.isFinished();
        log.info("MoveElevatorCommand is Finished : " + isFinished);
        return isFinished;
    }

    @Override
    protected void end() {
        log.info("Ending MoveElevatorCommand " + timeSinceInitialized());
    }
}
