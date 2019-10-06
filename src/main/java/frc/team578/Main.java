package frc.team578;

import edu.wpi.first.wpilibj.RobotBase;
import frc.team578.test.SwerveSteerTest;

public final class Main {
    public Main() {
    }

    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }
}
