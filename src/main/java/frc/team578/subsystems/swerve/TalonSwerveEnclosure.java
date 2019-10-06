package frc.team578.robot.subsystems.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;

public class TalonSwerveEnclosure implements UpdateDashboard {

    private String name;

    private WPI_TalonSRX driveTalon;
    private WPI_TalonSRX steerTalon;

    private boolean reverseEncoder = false;
    private boolean reverseSteer = false;
    private int trueNorthEncoderOffset;

    TalonSwerveEnclosure(String name, WPI_TalonSRX driveMotor, WPI_TalonSRX steerMotor, int trueNorth) {

        this.name = name;
        this.driveTalon = driveMotor;
        this.steerTalon = steerMotor;

        trueNorthEncoderOffset = trueNorth;

    }

    public WPI_TalonSRX getDriveTalon() {
        return this.driveTalon;
    }

    public WPI_TalonSRX getSteerTalon() {
        return this.steerTalon;
    }

    public void stopAllTalons() {
        this.steerTalon.stopMotor();
        this.driveTalon.stopMotor();
    }

    public void setSensorToAnalogPos() {
        this.steerTalon.setSelectedSensorPosition(this.steerTalon.getSensorCollection().getAnalogInRaw());
    }

    public void orientSensor()
    {
        int where = this.steerTalon.getSensorCollection().getAnalogInRaw()-trueNorthEncoderOffset;
        if(where<0) {
            where += 1024;
        }
        where%=1024;
        this.steerTalon.setSelectedSensorPosition(where);
    }

    // ------------ Steer Related

    /**
     * Set the angle for the steer motor
     *
     * @param angle the angle value: -0.5 - counterclockwise 180 degrees, 0 - forward 180 degrees, +0.5 - 180 degrees clockwise
     */
    public void moveToSteerAngle(double angle) {
        steerTalon.set(ControlMode.Position, angle * SwerveConstants.MAX_ENC_VAL * (reverseSteer ? -1 : 1));
    }

    public void moveSteerToEncoderPosition(int encPos) {
        steerTalon.set(ControlMode.Position, encPos);
    }

    public void moveSteerTrueNorth() {
        this.moveSteerToEncoderPosition(trueNorthEncoderOffset);
    }

    public int getSteerEncPosition() {
        return steerTalon.getSelectedSensorPosition(0) * (reverseEncoder ? -1 : 1);
    }

    public void resetSteerEncPosition(int position) {
        steerTalon.setSelectedSensorPosition(position);
    }

    public void zeroSteerEncoder() {
        this.resetSteerEncPosition(0);
    }

    public double getSteerCLT(int id) {
        return this.steerTalon.getClosedLoopTarget(id);
    }

    public double getSteerTurnCLTError() {
        return this.steerTalon.getClosedLoopError();
    }

    public double getSteerCLTErrorAbs() {
        return Math.abs(this.getSteerTurnCLTError());
    }

    public double getSteerErrorDerivitive() {
        return this.steerTalon.getErrorDerivative();
    }

    public double getSteerErrorDerivitiveAbs() {
        return Math.abs(this.getSteerErrorDerivitive());
    }

    // ------------ Drive Related

    /**
     * Set the value of the drive motor
     *
     * @param speed the speed value to set: -1 - full backwards, 0 - stop, +1 - full forward
     */
    public void setDriveSpeed(double speed) {
        driveTalon.set(ControlMode.PercentOutput, speed);
    }

    public double getDriveCLT(int id) {
        return this.driveTalon.getClosedLoopTarget(id);
    }

    public double getDriveTurnCLTError(int id) {
        return this.driveTalon.getClosedLoopError(id);
    }


    /**
     * @param speed: the speed to move the wheel, -1.0 being full backwards, 0 being stop +1.0 being full forward
     * @param angle: the angle to turn the wheel, 0 being forward, -1.0 being full turn counterclockwise, +1.0 being full turn clockwise
     */
    public void move(double speed, double angle) {
        int encPosition = getSteerEncPosition();

        angle = convertAngle(angle, encPosition);

        if (shouldReverse(angle, encPosition)) {
            if (angle < 0)
                angle += 0.5;
            else
                angle -= 0.5;

            speed *= -1.0;
        }

        setDriveSpeed(speed);

        if (speed != 0.0) {
            moveToSteerAngle(angle);
        }
    }

    public String getName() {
        return name;
    }


    private boolean shouldReverse(double wa, double encoderValue) {

        double ea = SwerveUtils.convertEncoderValue(encoderValue);

        //Convert the next wheel angle, which is from -.5 to .5, to 0 to 1
        if (wa < 0) wa += 1;

        //Find the difference between the two (not sure if the conversion from (-0.5 to 0.5) to (0 to 1) above is needed)
        //Difference between the two points. May be anything between -1 to 1, but we are looking for a number between -.5 to .5
        double longDifference = Math.abs(wa - ea);

        //finds shortest distance (0 to 0.5), always positive though (which is what we want)
        double difference = Math.min(longDifference, 1.0 - longDifference);

        //If the difference is greater than 1/4, then return true (aka it is easier for it to turn around and go backwards than go forward)
        return difference > 0.25;
    }

    private double convertAngle(double angle, double encoderValue) {
        //angles are between -.5 and .5
        //This is to allow the motors to rotate in continuous circles (pseudo code on the Team 4048 forum)
        double encPos = encoderValue / SwerveConstants.MAX_ENC_VAL;

        double temp = angle;
        temp += (int) encPos;

        encPos = encPos % 1;

        if ((angle - encPos) > 0.5) temp -= 1;

        if ((angle - encPos) < -0.5) temp += 1;

        return temp;
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putData(steerTalon);
        SmartDashboard.putData(driveTalon);


        SmartDashboard.putNumber(name + ".steert.araw", steerTalon.getSensorCollection().getAnalogInRaw());
        SmartDashboard.putNumber(name + ".steert.senspos", steerTalon.getSelectedSensorPosition());
        SmartDashboard.putNumber(name + ".steert.tn", trueNorthEncoderOffset);

        if (steerTalon.getControlMode() == ControlMode.Position) {
            SmartDashboard.putNumber(name + ".steer.encpos", this.getSteerEncPosition());
            SmartDashboard.putNumber(name + ".steert.CLT", steerTalon.getClosedLoopTarget());
            SmartDashboard.putNumber(name + ".steert.CLE", steerTalon.getClosedLoopError());
            SmartDashboard.putNumber(name + ".steert.errd", steerTalon.getErrorDerivative());
        }
//        SmartDashboard.putNumber(name + ".drivet.araw",driveTalon.getSensorCollection().getAnalogInRaw());
//        SmartDashboard.putNumber(name + ".drivet.senspos",driveTalon.getSelectedSensorPosition());
//        SmartDashboard.putNumber(name + ".drivet.CLE",driveTalon.getClosedLoopError());
//        SmartDashboard.putNumber(name + ".drivet.CLT",driveTalon.getClosedLoopTarget());


    }
}
