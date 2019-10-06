package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import frc.team578.robot.enums.ElevatorPositionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveElevatorAnalogCommand extends Command {


    private static final Logger log = LogManager.getLogger(MoveElevatorAnalogCommand.class);

    public static final double SCALE_FACTOR = 0.6;

    public MoveElevatorAnalogCommand() {
        log.info("MoveElevatorAnalogCommand Constructor");
        requires(Robot.elevatorSubsystem);
    }

    @Override
    protected void initialize() {
        log.info("MoveElevatorAnalogCommand MoveElevatorCommand");
    }

    @Override
    protected void execute() {
        Robot.elevatorSubsystem.moveArmMotor(Robot.oi.gp1.getPadLeftY() * SCALE_FACTOR);
        Robot.elevatorSubsystem.moveStructureMotor(Robot.oi.gp1.getPadRightY() * SCALE_FACTOR);
    }

    @Override
    protected void interrupted() {
        log.info("Interrupted MoveElevatorAnalogCommand");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        log.info("Ending MoveElevatorAnalogCommand " + timeSinceInitialized());
    }
}
