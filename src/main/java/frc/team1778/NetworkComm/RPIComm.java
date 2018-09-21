package frc.team1778.NetworkComm;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class RPIComm {

  private static boolean initialized = false;

  // comm objects
  private static NetworkTable table;
  private static NetworkTableInstance tableInstance;

  // camera image parameters
  private static int frameWidth = 320;
  private static int frameHeight = 240;

  public static void initialize() {

    if (initialized) return;

    InputOutputComm.initialize();

    // if using Roborio-hosted network table
    // tableInstance = NetworkTableInstance.getDefault();

    // if using RPi-hosted network table
    tableInstance = NetworkTableInstance.create();
    tableInstance.setNetworkIdentity("Roborio_RPI_Client");
    tableInstance.startClient("10.17.78.10", 1735);

    table = tableInstance.getTable("RPIComm/Data_Table");

    initialized = true;
  }

  public static void autoInit() {

    reset();
  }

  public static void teleopInit() {

    reset();
  }

  public static void disabledInit() {}

  public static void reset() {}

  public static void setBoolean(String key, boolean value) {
    table.getEntry(key).setBoolean(value);
  }

  public static void setDouble(String key, double value) {
    table.getEntry(key).setDouble(value);
  }
}
