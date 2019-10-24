package frc.team578.robot;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main {
    public Main() {
    }

    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }
}
