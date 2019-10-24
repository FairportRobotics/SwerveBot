package frc.team578.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team578.robot.commands.*;
import frc.team578.robot.subsystems.interfaces.Initializable;
import frc.team578.robot.utils.Gamepad;

public class OI implements Initializable {

    public Joystick leftJoystick = new Joystick(2);
    public Joystick rightJoystick = new Joystick(3);
    public GP gp1 = new GP(RobotMap.CONTROL_GAMEPAD_ID); // Elevator and arm functions
    public GP gp2 = new GP(RobotMap.ELEVATOR_GAMEPAD_ID); // Climber functions
//
//
//            double lastXValue;
//            public double getStrafe() {
//                if (!leftJoystick.getTrigger()) {
//                    lastXValue = leftJoystick.getX();
//        }
//        return lastXValue;
//    }

    public void initialize() {




        if(leftJoystick.getTrigger()) { new CalibrateDrivesCommand();}

    }

    // This is here to make buttons persistant (i.e. Gamepad makes a new instance every request
    // TODO : Want to fix that at some point.
    public class GP {

        Gamepad gamepad;
        JoystickButton rb;
        JoystickButton lb;
        JoystickButton rt;
        JoystickButton lt;
        JoystickButton buttonA;
        JoystickButton buttonB;
        JoystickButton buttonX;
        JoystickButton buttonY;
        JoystickButton back;
        JoystickButton start;
        boolean dpadLeft;

        // Gamepad controls


        public GP(int id) {
            gamepad = new Gamepad(id);
            rb = gamepad.getRightShoulder();
            lb = gamepad.getLeftShoulder();
            rt = gamepad.getRightTriggerClick();
            lt = gamepad.getLeftTriggerClick();
            buttonA = gamepad.getButtonA();
            buttonB = gamepad.getButtonB();
            buttonX = gamepad.getButtonX();
            buttonY = gamepad.getButtonY();
            back = gamepad.getBackButton();
            start = gamepad.getStartButton();
            boolean dpadLeft = gamepad.getDPadLeft();
        }


        public double getPadLeftX() {
            return gamepad.getLeftX();
        }

        public double getPadLeftY() {
            return gamepad.getLeftY();
        }

        public double getPadRightX() {
            return gamepad.getRightX();
        }

        public double getPadRightY() {
            return gamepad.getRightY();
        }
    }
}