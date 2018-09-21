package frc.team1778.NetworkComm;

// import edu.wpi.first.wpilibj.networktables.NetworkTable;  // deprecated in 2018

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class InputOutputComm {

  private static boolean initialized = false;
  static final String PI_ADDRESS = "10.17.78.10";
  static final int PORT = 1181; // or whatever it ends up being

  public static void initialize() {
    if (initialized) return;

    // get default local network table
    tableInstance = NetworkTableInstance.getDefault();
    table = tableInstance.getTable("InputOutput1778/DataTable");

    // publish the address of the Raspberry Pi camera stream
    tableInstance
        .getEntry("/CameraPublisher/PiCamera/streams")
        .setStringArray(
            new String[] {"mjpeg:http://" + PI_ADDRESS + ":" + PORT + "/?action=stream"});

    initialized = true;
  }

  public static enum LogTable {
    kMainLog,
    kRPICommLog,
    kDriveLog
  };

  // instance data and methods
  private static NetworkTableInstance tableInstance;
  private static NetworkTable table;

  public static void putBoolean(LogTable log, String key, boolean value) {

    if (table != null) table.getEntry(key).setBoolean(value);
    else System.out.println("No network table to write to!!");
  }

  public static void putDouble(LogTable log, String key, double value) {
    if (table != null) table.getEntry(key).setDouble(value);
    else System.out.println("No network table to write to!!");
  }

  public static void putInt(LogTable log, String key, int value) {
    if (table != null) table.getEntry(key).setNumber(value);
    else System.out.println("No network table to write to!!");
  }

  public static void putString(LogTable log, String key, String outputStr) {
    if (table != null) table.getEntry(key).setString(outputStr);
    else System.out.println("No network table to write to!!");
  }

  public static void deleteKey(String key) {
    if (table != null) table.delete(key);
    else System.out.println("No network table to write to!!");
  }
}
