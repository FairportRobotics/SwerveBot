package frc.team578.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team578.robot.RobotMap;
import frc.team578.robot.commands.MoveElevatorAnalogCommand;
import frc.team578.robot.enums.ElevatorPositionEnum;
import frc.team578.robot.subsystems.interfaces.Initializable;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;
import frc.team578.robot.utils.PIDFinished;

public class ElevatorSubsystem extends Subsystem implements Initializable, UpdateDashboard {

    public final int ARM_LEVEL_ONE_POS = 0;
    public final int STRUCTURE_LEVEL_ONE_POS = 0;

    private WPI_TalonSRX armTalon;
    private WPI_TalonSRX structureTalon;

    private PIDFinished<Double> pfArm;
    private PIDFinished<Double> pfStructure;

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new MoveElevatorAnalogCommand());
    }

    @Override
    public void initialize() {

        armTalon = new WPI_TalonSRX(RobotMap.ELEVATOR_ARM_TALON);
        armTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 0);
        armTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 0);
        armTalon.setNeutralMode(NeutralMode.Brake);
        armTalon.configNominalOutputForward(0, 0);
        armTalon.configNominalOutputReverse(0, 0);
        armTalon.configPeakOutputForward(1, 0);
        armTalon.configPeakOutputReverse(-1, 0);
            armTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                    LimitSwitchNormal.NormallyClosed, 0);
            armTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                    LimitSwitchNormal.NormallyClosed, 0);

        structureTalon = new WPI_TalonSRX(RobotMap.ELEVATOR_STRUCTURE_TALON);
        structureTalon.setNeutralMode(NeutralMode.Brake);
        structureTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 0);
        structureTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 0);
        structureTalon.setNeutralMode(NeutralMode.Brake);
        structureTalon.configNominalOutputForward(0, 0);
        structureTalon.configNominalOutputReverse(0, 0);
        structureTalon.configPeakOutputForward(1, 0);
        structureTalon.configPeakOutputReverse(-1, 0);
        structureTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyClosed, 0);
        structureTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyClosed, 0);


        // TODO: WE NEED TO ADD PID!!!!!!!!!

//        int talonID = 7;
//        boolean revMotor = false;
//        double pCoeff = 10;
//        double iCoeff = 0;
//        double dCoeff = 0;
//        double fCoeff = 0;
//        int iZone = 0;
//
//        armTalon = TalonUtil.createPIDTalon(talonID, revMotor, pCoeff, iCoeff, dCoeff, fCoeff, iZone);
//        structureTalon = TalonUtil.createPIDTalon(talonID, revMotor, pCoeff, iCoeff, dCoeff, fCoeff, iZone);
//


//        pfArm = new PIDFinished<Double>(50, 3, armTalon::getErrorDerivative, x -> x == 0);
//        pfStructure = new PIDFinished<Double>(50, 3, structureTalon::getErrorDerivative, (x) -> x == 0);
    }

    public void moveArmMotor(double value) {
        armTalon.set(ControlMode.PercentOutput, value);
    }

    public void moveStructureMotor(double value) {
        structureTalon.set(ControlMode.PercentOutput, value);
    }


    public void moveToLevel(ElevatorPositionEnum pos) {
        switch (pos) {
            case LEVEL_ONE:
                armTalon.set(ControlMode.Position, ARM_LEVEL_ONE_POS);
                structureTalon.set(ControlMode.Position, STRUCTURE_LEVEL_ONE_POS);
                break;
            default:
                // no-op
                break;
        }
    }

    public ElevatorPositionEnum getPosition() {
        if (armTalon.getSelectedSensorPosition() == ARM_LEVEL_ONE_POS && structureTalon.getSelectedSensorPosition() == STRUCTURE_LEVEL_ONE_POS) {
            return ElevatorPositionEnum.LEVEL_ONE;
        } else {
            return ElevatorPositionEnum.UNKNOWN;
        }
    }


    public boolean isFinished() {
        return pfArm.getFinished() && pfStructure.getFinished();
    }

    @Override
    public void updateDashboard() {
    }


}
