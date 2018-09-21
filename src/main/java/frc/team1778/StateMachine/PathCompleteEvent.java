package frc.team1778.StateMachine;

import frc.team1778.NetworkComm.InputOutputComm;
import frc.team1778.Systems.FreezyPath;

public class PathCompleteEvent extends Event {
  private String name;

  public PathCompleteEvent() {
    this.name = "<Path Complete Event>";
    InputOutputComm.initialize();
  }

  // overloaded initialize method
  public void initialize() {
    // System.out.println("PathCompleteEvent initialized!");

    super.initialize();
  }

  // overloaded trigger method
  public boolean isTriggered() {
    if (FreezyPath.isFinished()) {
      System.out.println("PathCompleteEvent triggered!");
      return true;
    }

    return false;
  }
}
