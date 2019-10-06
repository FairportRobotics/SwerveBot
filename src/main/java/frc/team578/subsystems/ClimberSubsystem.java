package frc.team578.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.RobotMap;
import frc.team578.robot.subsystems.interfaces.Initializable;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;

public class ClimberSubsystem extends Subsystem implements Initializable, UpdateDashboard {

    private Solenoid frontSolenoids;
    private Solenoid rearSolenoids;

    // flts = front left top sensor, flbs = front left bottom sensor
    DigitalInput flts;// = new DigitalInput(0);
    DigitalInput flbs;// = new DigitalInput(0);
    DigitalInput frts;// = new DigitalInput(0);
    DigitalInput frbs;// = new DigitalInput(0);
    DigitalInput blts;// = new DigitalInput(0);
    DigitalInput blbs;// = new DigitalInput(0);
    DigitalInput brts;// = new DigitalInput(0);
    DigitalInput brbs;// = new DigitalInput(0);
    DigitalInput fps = new DigitalInput(8);
    DigitalInput bps = new DigitalInput(9);


    private WPI_TalonSRX climbWheelsTalon;
    private double wheel_power = .5;

//    private DoubleSolenoid.Value frontRetract = DoubleSolenoid.Value.kForward;
//    private DoubleSolenoid.Value frontExtend = DoubleSolenoid.Value.kReverse;
//
//    private DoubleSolenoid.Value backRetract = DoubleSolenoid.Value.kForward;
//    private DoubleSolenoid.Value backExtend = DoubleSolenoid.Value.kReverse;


    @Override
    protected void initDefaultCommand() {
    }

    @Override
    public void initialize() {
        frontSolenoids = new Solenoid(RobotMap.PCM, RobotMap.PCM_FRONT_CLIMB);
        rearSolenoids = new Solenoid(RobotMap.PCM, RobotMap.PCM_REAR_CLIMB);
        climbWheelsTalon = new WPI_TalonSRX(RobotMap.CLIMB_WHEELS_TALON_ID);
    }

    public void retractFrontClimber() {
        frontSolenoids.set(false);
    }

    public void extendFrontClimber() {
        frontSolenoids.set(true);
    }

    public void retractRearClimber() {
        rearSolenoids.set(false);
    }

    public void extendRearClimber() {
        rearSolenoids.set(true);
    }

//    public boolean isFrontClimberRetracted() {
//        return frontSolenoids.get() == frontRetract && flts.get() && frts.get();
//    }

//    public boolean isFrontClimberExtended() {
//        return frontSolenoids.get() == frontExtend && flbs.get() && frbs.get();
//    }
//
//    public boolean isRearClimberRetracted() {
//        return rearSolenoids.get() == backRetract && blts.get() && brts.get();
//    }

//    public boolean isRearClimberExtended() {
//        return rearSolenoids.get() == backExtend && blbs.get() && brbs.get();
//    }

    public void climbWheelsForward() {
        climbWheelsTalon.set(ControlMode.PercentOutput, -wheel_power);
    }

    public void climbWheelsBackwards() {
        climbWheelsTalon.set(ControlMode.PercentOutput, wheel_power);
    }

    public void climbWheelsStop() {
        climbWheelsTalon.set(ControlMode.PercentOutput, 0);
    }


    @Override
    public void updateDashboard() {
        SmartDashboard.putData("climb.fs",frontSolenoids);
        SmartDashboard.putData("climb.rs",rearSolenoids);
        SmartDashboard.putData("climb.cwtal",climbWheelsTalon);
        SmartDashboard.putData("fps",fps);
        SmartDashboard.putData("bps",bps);
    }
}
