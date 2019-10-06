package frc.team578.robot.commands.group;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team578.robot.commands.MoveArmPIDCommand;
import frc.team578.robot.commands.MoveElevatorCommand;
import frc.team578.robot.enums.ArmPositionEnum;
import frc.team578.robot.enums.ElevatorPositionEnum;

public class MoveEAToPosition extends CommandGroup {
    public MoveEAToPosition(ElevatorPositionEnum elevatorPos, ArmPositionEnum armPos) {
        addSequential(new MoveElevatorCommand(elevatorPos));
        addSequential(new MoveArmPIDCommand(armPos));
    }
}
