package frc.team578.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team578.robot.commands.SwerveDriveCommand;
import frc.team578.robot.subsystems.interfaces.Initializable;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;
import frc.team578.robot.subsystems.swerve.SwerveDrive;

public class SwerveDriveSubsystem extends Subsystem implements Initializable, UpdateDashboard {

    private SwerveDrive swerveDrive;

    @Override
    protected void initDefaultCommand() {
        // This is where the swerve command starts
        setDefaultCommand(new SwerveDriveCommand());
    }

    @Override
    public void initialize() {
        swerveDrive = SwerveDrive.create();
    }

    public void move(double fwd, double str, double rot, double angleDeg) {
        swerveDrive.move(fwd, str, rot, angleDeg);
    }

    public void zeroAllSteerEncoders() {
        swerveDrive.zeroAllSteerEncoders();
    }

    public void stop() {
        swerveDrive.stop();
    }

    @Override
    public void updateDashboard() {

//        SmartDashboard.putNumber("sd.derivsum",getSteerErrorDerivitiveSum());

        swerveDrive.updateDashboard();
    }

    public double getSteerCLTErrorSum() {
        return swerveDrive.getSteerCLTErrorSum();
    }

    public double getSteerErrorDerivitiveSum() {
        return swerveDrive.getSteerErrorDerivitiveSum();
    }

    public void calibrateAllSteerEncoders() {
        swerveDrive.calibrateAllSteerEncoders();
    }
}
