package frc.team1778.StateMachine;

import frc.team1778.ChillySwerve.ChillySwerve;
import frc.team1778.NetworkComm.InputOutputComm;
import frc.team1778.Systems.FreezyPath;
import frc.team1778.Systems.NavXSensor;

public class FollowPathAction extends Action {

  private String name;
  private int pathToFollow;

  public FollowPathAction(int pathToFollow) {
    this.name = "<Follow Path Action>";
    this.pathToFollow = pathToFollow;

    ChillySwerve.initialize();
    NavXSensor.initialize();
    InputOutputComm.initialize();
    FreezyPath.initialize();
    FreezyPath.reset(pathToFollow);
  }

  public FollowPathAction(String name, int pathToFollow) {
    this.name = name;
    this.pathToFollow = pathToFollow;

    ChillySwerve.initialize();
    NavXSensor.initialize();
    InputOutputComm.initialize();
    FreezyPath.initialize();
    FreezyPath.reset(pathToFollow);
  }

  // action entry
  public void initialize() {
    // do some drivey initialization

    ChillySwerve.autoInit(true, 0.0, false);
    FreezyPath.start();

    super.initialize();
  }

  // called periodically
  public void process() {

    // no action needed (Path Following is self-running)

    super.process();
  }

  // state cleanup and exit
  public void cleanup() {
    // do some drivey cleanup

    FreezyPath.stop();
    ChillySwerve.autoStop();

    // cleanup base class
    super.cleanup();
  }
}
