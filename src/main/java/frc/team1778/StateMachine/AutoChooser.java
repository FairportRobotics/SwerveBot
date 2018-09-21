package frc.team1778.StateMachine;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {

  //  action type selection
  public static final int DO_NOTHING = 0;
  public static final int DRIVE_FORWARD = 1;
  public static final int FOLLOW_PATH1 = 2;
  public static final int FOLLOW_PATH2 = 3;
  public static final int FOLLOW_PATH3 = 4;

  // internal selection class used for SendableChooser only
  public class ModeSelection {
    public int mode = DO_NOTHING;

    ModeSelection(int mode) {
      this.mode = mode;
    }
  }

  int mode;

  private SendableChooser<ModeSelection> chooser_action;

  public AutoChooser() {

    // action chooser setup
    chooser_action = new SendableChooser<ModeSelection>();
    chooser_action.addDefault("DO_NOTHING", new ModeSelection(DO_NOTHING));
    chooser_action.addObject("DRIVE_FORWARD", new ModeSelection(DRIVE_FORWARD));
    chooser_action.addObject("FOLLOW_PATH1", new ModeSelection(FOLLOW_PATH1));
    chooser_action.addObject("FOLLOW_PATH2", new ModeSelection(FOLLOW_PATH2));
    chooser_action.addObject("FOLLOW_PATH3", new ModeSelection(FOLLOW_PATH3));
    SmartDashboard.putData("AutoChooser_Action", chooser_action);
  }

  public int getAction() {

    // check action chooser
    ModeSelection action_selection = chooser_action.getSelected();
    if (action_selection.mode != DO_NOTHING) return action_selection.mode;

    // default - do nothing
    return DO_NOTHING;
  }
}
