package frc.team578.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team578.robot.RobotMap;
import frc.team578.robot.enums.ArmPositionEnum;
import frc.team578.robot.subsystems.interfaces.Initializable;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;

public class ArmSubsystem  extends Subsystem implements Initializable, UpdateDashboard {

    private DoubleSolenoid firstSolenoids;
    private DoubleSolenoid secondSolenoids;

    // fsts == first solenoid top sensor
    DigitalInput fsts;// = new DigitalInput(1);
    DigitalInput fsbs;// = new DigitalInput(2);
    DigitalInput ssts;// = new DigitalInput(3);
    DigitalInput ssbs;// = new DigitalInput(4);

    private Solenoid top = new Solenoid(RobotMap.PCM, RobotMap.PCM_ARM_TOP);
    private Solenoid bottom = new Solenoid(RobotMap.PCM, RobotMap.PCM_ARM_BOTTOM);

//    private DoubleSolenoid.Value firstRetract = Solenoid.Value.kForward;
//    private DoubleSolenoid.Value firstExtend = DoubleSolenoid.Value.kReverse;
//
//    private DoubleSolenoid.Value secondRetract = DoubleSolenoid.Value.kForward;
//    private DoubleSolenoid.Value secondExtend = DoubleSolenoid.Value.kReverse;

    @Override
    public void initialize() {
//        firstSolenoids = new DoubleSolenoid(RobotMap.PCM, RobotMap.PCM_ARM_FIRST_EXTEND, RobotMap.PCM_ARM_FIRST_RETRACT);
//        secondSolenoids = new DoubleSolenoid(RobotMap.PCM, RobotMap.PCM_ARM_SECOND_EXTEND, RobotMap.PCM_ARM_SECOND_RETRACT);
    }

    public void retract() {
        top.set(false);
        bottom.set(false);
    }

    public void extendMid() {
        top.set(false);
        bottom.set(true);
    }

    public void extendMid2() {
        top.set(true);
        bottom.set(false);
    }

    public void extendFull() {
        top.set(true);
        bottom.set(true);
    }

    public boolean isArmMoving() {
        // TODO : Or make this positional like Climber?
        return false;
    }

//    public ArmPositionEnum getArmPosition() {
//        if (firstSolenoids.get() == firstExtend && secondSolenoids.get() == secondExtend) {
//            return ArmPositionEnum.FULL_EXTEND;
//        } else if (firstSolenoids.get() == firstExtend && secondSolenoids.get() == secondRetract) {
//            return ArmPositionEnum.MID_EXTEND;
//        } else if (firstSolenoids.get() == firstRetract && secondSolenoids.get() == secondExtend) {
//            return ArmPositionEnum.MID2_EXTEND;
//        } else {
//            return ArmPositionEnum.RETRACTED;
//        }
//    }

    @Override
    public void updateDashboard() {

    }

    @Override
    protected void initDefaultCommand() {

    }
}
