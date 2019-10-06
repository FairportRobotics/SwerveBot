package frc.team578.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.subsystems.interfaces.Initializable;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GyroSubsystem extends Subsystem implements Initializable, UpdateDashboard {

    private static final Logger log = LogManager.getLogger(GyroSubsystem.class);

    private AHRS navx;

    @Override
    protected void initDefaultCommand() {
    }

    public GyroSubsystem(String name) {
        super(name);
    }

    public void initialize() {

        log.info("Init " + this.getClass().getName());

        try {
            navx = new AHRS(SPI.Port.kMXP);

            this.setToZero();
        } catch (RuntimeException ex) {
            log.error("Error instantiating navX-MXP:  " + ex.getMessage(),ex);
        }
    }

    public double getHeading() {
        double angle = navx.getFusedHeading();

        return angle;
    }

    public void setToZero() {
        navx.zeroYaw();
    }

    public double getAnglePidGet() {
        return navx.pidGet();
    }

    public double getRate() {
        // Return the rate of rotation of the yaw (Z-axis) gyro, in degrees per second.
        return navx.getRate();
    }

    public boolean isConnected() {
        return navx.isConnected();
    }

    public void reset() {
        navx.reset();
    }

    @Override
    public void updateDashboard() {
        SmartDashboard.putNumber("gyro.heading", getHeading());
    }
}
