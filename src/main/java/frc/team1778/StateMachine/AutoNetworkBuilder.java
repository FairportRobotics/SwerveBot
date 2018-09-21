package frc.team1778.StateMachine;

import frc.team1778.Systems.FreezyPath;
import java.util.ArrayList;

public class AutoNetworkBuilder {

  public static final int DO_NOTHING = 0;
  public static final int DRIVE_FORWARD = 1;
  public static final int FOLLOW_PATH1 = 2;
  public static final int FOLLOW_PATH2 = 3;
  public static final int FOLLOW_PATH3 = 4;

  // closed-loop position cruise velocity and acceleration (used for all closed-loop position
  // control)
  // units are RPM

  // ~3 ft/s - FAST
  private static final int CLOSED_LOOP_VEL_FAST = 900;
  private static final int CLOSED_LOOP_ACCEL_FAST = 300;

  // ~2 ft/s - SLOW
  private static final int CLOSED_LOOP_VEL_SLOW = 850;
  private static final int CLOSED_LOOP_ACCEL_SLOW = 300;

  // ~1 ft/s - VERY SLOW
  private static final int CLOSED_LOOP_VEL_VERY_SLOW = 400;
  private static final int CLOSED_LOOP_ACCEL_VERY_SLOW = 200;

  private static ArrayList<AutoNetwork> autoNets;

  private static boolean initialized = false;

  public static void initialize() {

    if (!initialized) {
      autoNets = null;

      initialized = true;
    }
  }

  public static ArrayList<AutoNetwork> readInNetworks() {

    if (!initialized) initialize();

    autoNets = new ArrayList<AutoNetwork>();

    // create networks
    autoNets.add(DO_NOTHING, createDoNothingNetwork());
    autoNets.add(DRIVE_FORWARD, createDriveForward());
    autoNets.add(FOLLOW_PATH1, createFollowPathNetwork1());
    autoNets.add(FOLLOW_PATH2, createFollowPathNetwork2());
    autoNets.add(FOLLOW_PATH3, createFollowPathNetwork3());

    return autoNets;
  }

  ///////////////////////////////////////////////////////////
  /*            AutoState Creation Methods                 */
  /*              Single Action States                     */
  /*            (These are used repeatedly)                */
  ///////////////////////////////////////////////////////////

  private static AutoState createIdleState(String state_name) {
    AutoState idleState = new AutoState(state_name);
    IdleAction deadEnd = new IdleAction("<Dead End Action>");
    DriveForwardAction driveForwardReset =
        new DriveForwardAction("<Drive Forward Action -reset>", 0.0, true, 0.0); // reset gyro
    idleState.addAction(deadEnd);
    idleState.addAction(driveForwardReset);

    return idleState;
  }

  private static AutoState createMagicDriveState(
      String state_name,
      double dist_inches,
      double error_inches,
      int max_vel_rpm,
      int max_accel_rpm) {
    AutoState driveState = new AutoState(state_name);
    DriveForwardMagicAction driveForwardMagicAction =
        new DriveForwardMagicAction(
            "<Drive Forward Magic Action>", dist_inches, max_vel_rpm, max_accel_rpm, true, 0.0);
    // TimeEvent timer = new TimeEvent(2.5);  // drive forward timer event - allow PID time to
    // settle
    ClosedLoopPositionEvent pos = new ClosedLoopPositionEvent(dist_inches, error_inches, 0.6);
    driveState.addAction(driveForwardMagicAction);
    // driveState.addEvent(timer);
    driveState.addEvent(pos);

    return driveState;
  }

  private static AutoState createTimerState(String state_name, double timer_sec) {
    AutoState timerState = new AutoState(state_name);
    IdleAction deadEnd = new IdleAction("<Dead End Action>");
    DriveForwardAction driveForwardReset =
        new DriveForwardAction("<Drive Forward Action -reset>", 0.0, true, 0.0); // reset gyro
    TimeEvent timer = new TimeEvent(timer_sec);
    timerState.addAction(deadEnd);
    timerState.addAction(driveForwardReset);
    timerState.addEvent(timer);

    return timerState;
  }

  ////////////////////////////////////////////////////////////

  // **** DO NOTHING Network *****
  private static AutoNetwork createDoNothingNetwork() {

    AutoNetwork autoNet = new AutoNetwork("<Do Nothing Network>");

    AutoState idleState = new AutoState("<Idle State>");
    IdleAction deadEnd = new IdleAction("<Dead End Action>");
    idleState.addAction(deadEnd);

    autoNet.addState(idleState);

    return autoNet;
  }

  // **** MOVE FORWARD Network *****
  // 1) drive forward for a number of sec
  // 2) go back to idle and stay there
  private static AutoNetwork createDriveForward() {

    AutoNetwork autoNet = new AutoNetwork("<Drive Forward Network>");

    // create states
    AutoState driveState =
        createMagicDriveState(
            "<Drive State 1>", 120.0, 3.0, CLOSED_LOOP_VEL_SLOW, CLOSED_LOOP_ACCEL_SLOW);
    AutoState idleState = createIdleState("<Idle State>");

    // connect the state sequence
    driveState.associateNextState(idleState);

    // add states to the network list
    autoNet.addState(driveState);
    autoNet.addState(idleState);

    return autoNet;
  }

  // **** FOLLOW PATH Network 1 *****
  // 1) follow a specified path until complete
  // 2) go back to idle and stay there
  private static AutoNetwork createFollowPathNetwork1() {

    AutoNetwork autoNet = new AutoNetwork("<Follow Path Network 1>");

    // create states
    AutoState followPathState = new AutoState("<Follow Path State>");
    FollowPathAction follow = new FollowPathAction("<Follow Path 1>", FreezyPath.PATH1);
    PathCompleteEvent pathComplete = new PathCompleteEvent();
    followPathState.addAction(follow);
    followPathState.addEvent(pathComplete);

    AutoState idleState = createIdleState("<Idle State>");

    // connect the state sequence
    followPathState.associateNextState(idleState);

    // add states to the network list
    autoNet.addState(followPathState);
    autoNet.addState(idleState);

    return autoNet;
  }

  // **** FOLLOW PATH Network 2 *****
  // 1) follow a specified path until complete
  // 2) go back to idle and stay there
  private static AutoNetwork createFollowPathNetwork2() {

    AutoNetwork autoNet = new AutoNetwork("<Follow Path Network 2>");

    // create states
    AutoState followPathState = new AutoState("<Follow Path State>");
    FollowPathAction follow = new FollowPathAction("<Follow Path 2>", FreezyPath.PATH2);
    PathCompleteEvent pathComplete = new PathCompleteEvent();
    followPathState.addAction(follow);
    followPathState.addEvent(pathComplete);

    AutoState idleState = createIdleState("<Idle State>");

    // connect the state sequence
    followPathState.associateNextState(idleState);

    // add states to the network list
    autoNet.addState(followPathState);
    autoNet.addState(idleState);

    return autoNet;
  }

  // **** FOLLOW PATH Network 1 *****
  // 1) follow a specified path until complete
  // 2) go back to idle and stay there
  private static AutoNetwork createFollowPathNetwork3() {

    AutoNetwork autoNet = new AutoNetwork("<Follow Path Network 3>");

    // create states
    AutoState followPathState = new AutoState("<Follow Path State>");
    FollowPathAction follow = new FollowPathAction("<Follow Path 3>", FreezyPath.PATH3);
    PathCompleteEvent pathComplete = new PathCompleteEvent();
    followPathState.addAction(follow);
    followPathState.addEvent(pathComplete);

    AutoState idleState = createIdleState("<Idle State>");

    // connect the state sequence
    followPathState.associateNextState(idleState);

    // add states to the network list
    autoNet.addState(followPathState);
    autoNet.addState(idleState);

    return autoNet;
  }

  /**
   * **************************************************************************************************
   */
  /**
   * ** DEBUG NETWORKS **** Networks below this are used only for debug - disable during competition
   * **
   */
  /**
   * **************************************************************************************************
   */

  // **** Test Network - does nothing except transitions states *****
  private static AutoNetwork createTestNetwork() {

    AutoNetwork autoNet = new AutoNetwork("<Test Network>");

    AutoState idleState = new AutoState("<Idle State 1>");
    IdleAction startIdle = new IdleAction("<Start Idle Action 1>");
    IdleAction doSomething2 = new IdleAction("<Placeholder Action 2>");
    IdleAction doSomething3 = new IdleAction("<Placeholder Action 3>");
    TimeEvent timer1 = new TimeEvent(10.0); // timer event
    idleState.addAction(startIdle);
    idleState.addAction(doSomething2);
    idleState.addAction(doSomething3);
    idleState.addEvent(timer1);

    AutoState idleState2 = new AutoState("<Idle State 2>");
    IdleAction startIdle2 = new IdleAction("<Start Idle Action 2>");
    IdleAction doSomething4 = new IdleAction("<Placeholder Action 4>");
    IdleAction doSomething5 = new IdleAction("<Placeholder Action 5>");
    TimeEvent timer2 = new TimeEvent(10.0); // timer event
    idleState2.addAction(startIdle2);
    idleState2.addAction(doSomething4);
    idleState2.addAction(doSomething5);
    idleState2.addEvent(timer2);

    AutoState idleState3 = new AutoState("<Idle State 3>");
    IdleAction startIdle3 = new IdleAction("<Start Idle Action 3>");
    IdleAction doSomething6 = new IdleAction("<Placeholder Action 6>");
    IdleAction doSomething7 = new IdleAction("<Placeholder Action 7>");
    TimeEvent timer3 = new TimeEvent(10.0); // timer event
    idleState3.addAction(startIdle3);
    idleState3.addAction(doSomething6);
    idleState3.addAction(doSomething7);
    idleState3.addEvent(timer3);

    AutoState idleState4 = new AutoState("<Idle State 4>");
    IdleAction deadEnd = new IdleAction("<Dead End Action>");
    idleState4.addAction(deadEnd);

    // connect each event with a state to move to
    idleState.associateNextState(idleState2);
    idleState2.associateNextState(idleState3);
    idleState3.associateNextState(idleState4);

    autoNet.addState(idleState);
    autoNet.addState(idleState2);
    autoNet.addState(idleState3);
    autoNet.addState(idleState4);

    return autoNet;
  }
}
