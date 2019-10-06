package frc.team578.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team578.robot.Robot;
import frc.team578.robot.enums.ArmPositionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveArmPIDCommand extends Command {

    private static final Logger log = LogManager.getLogger(MoveArmPIDCommand.class);

    ArmPositionEnum positionTarget;

    public MoveArmPIDCommand(ArmPositionEnum positionTarget) {
        this.positionTarget = positionTarget;
        log.info("MoveArmPIDCommand Constructor");
    }

    @Override
    protected void initialize() {
        log.info("Initializing MoveArmPIDCommand");
    }

    @Override
    protected void execute() {
        log.info("Exec MoveArmPIDCommand");
        if (positionTarget == ArmPositionEnum.RETRACTED) {
            Robot.armSubsystem.retract();
        } else if (positionTarget == ArmPositionEnum.MID_EXTEND) {
            Robot.armSubsystem.extendMid();
        } else if (positionTarget == ArmPositionEnum.MID2_EXTEND) {
            Robot.armSubsystem.extendMid2();
        } else if (positionTarget == ArmPositionEnum.FULL_EXTEND) {
            Robot.armSubsystem.extendFull();
        }
    }


    @Override
    protected void interrupted() {
        log.info("Interrupted MoveArmPIDCommand");
    }

    @Override
    protected boolean isFinished() {

//        Robot.armSubsystem.getArmPosition();

        boolean isFinished = true;
        log.info("MoveArmPIDCommand is Finished : " + isFinished);
        return isFinished;
    }

    @Override
    protected void end() {
        log.info("Ending MoveArmPIDCommand " + timeSinceInitialized());
    }
}
