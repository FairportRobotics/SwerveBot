package frc.team1778.Utility;

public class HardwareIDs {

  // motor controller IDs
  public static final int FRONT_LEFT_DRIVE_TALON_ID = 1;
  public static final int FRONT_RIGHT_DRIVE_TALON_ID = 2;
  public static final int BACK_LEFT_DRIVE_TALON_ID = 3;
  public static final int BACK_RIGHT_DRIVE_TALON_ID = 4;

  public static final int FRONT_LEFT_ROTATE_TALON_ID = 11;
  public static final int FRONT_RIGHT_ROTATE_TALON_ID = 12;
  public static final int BACK_LEFT_ROTATE_TALON_ID = 13;
  public static final int BACK_RIGHT_ROTATE_TALON_ID = 14;

  // swerve controller joystick IDs
  public static final int CONTROL_GAMEPAD_ID = 0;
  public static final int LEFT_X_AXIS = 0;
  public static final int LEFT_Y_AXIS = 1;
  public static final int RIGHT_X_AXIS = 4;
  public static final int RIGHT_Y_AXIS = 5;

  // encoder variables
  public static final int ENCODER_PULSES_PER_REV = 256 * 4; // 63R  - on the competition bot motors
  public static final double WHEEL_DIAMETER_INCHES = 5.9;
  public static final double INCHES_PER_REV =(WHEEL_DIAMETER_INCHES * 3.14159); // 5.9-in diameter wheel (worn)

  public static final double INCHES_PER_ENCODER_PULSE = INCHES_PER_REV / ENCODER_PULSES_PER_REV;
  public static final double RPM_TO_UNIT_PER_100MS = ENCODER_PULSES_PER_REV / (60 * 10);
}
