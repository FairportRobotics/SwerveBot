package frc.team1778.StateMachine;

import frc.team1778.ChillySwerve.ChillySwerve;
import frc.team1778.Systems.NavXSensor;

public class TurnAction extends Action {

  private double angleToTurn = 0.0;
  private double speedToTurn = 0.3;
  private boolean resetGyro = true;
  private double headingDeg = 0.0; // heading to use if not resetting gyro
  private double initialAngle = 0.0;

  public TurnAction(double angleToTurn, double speed, boolean resetGyro) {
    this.name = "<Turn Action>";
    this.angleToTurn = angleToTurn; // absolute heading to use if not resetting gyro
    this.speedToTurn = speed;
    this.resetGyro = resetGyro;

    ChillySwerve.initialize();
    NavXSensor.initialize();
  }

  public TurnAction(String name, double angleToTurn, double speed, boolean resetGyro) {
    this.name = name;
    this.angleToTurn = angleToTurn; // absolute heading to use if not resetting gyro
    this.speedToTurn = speed;
    this.resetGyro = resetGyro;

    ChillySwerve.initialize();
    NavXSensor.initialize();
  }

  // action entry
  public void initialize() {

    // if we're not resetting the gyro, we'll want to see what angle it is to start
    if (resetGyro) {
      NavXSensor.reset();
      initialAngle = 0.0;
    } else initialAngle = NavXSensor.getAngle();

    // initialize motor assembly for auto
    ChillySwerve.autoInit(resetGyro, initialAngle, false);

    super.initialize();
  }

  // called periodically
  public void process() {

    // check the difference from our initial angle
    double angleDiff = angleToTurn - initialAngle;

    // rotate to close the gap
    if (angleDiff > 0.0) ChillySwerve.rotateRight(speedToTurn);
    else ChillySwerve.rotateLeft(speedToTurn);

    super.process();
  }

  // action cleanup and exit
  public void cleanup() {
    // do some drivey cleanup

    // PWMDriveAssembly not supported

    ChillySwerve.autoStop();

    // cleanup base class
    super.cleanup();
  }
}
