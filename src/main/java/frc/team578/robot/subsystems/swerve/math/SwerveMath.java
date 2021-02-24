package frc.team578.robot.subsystems.swerve.math;

import java.util.Arrays;
import java.util.List;

/**
 * This is the main class for the swerveDrive library.
 * The library handles the calculations required to drive a robot using SwerveDrive wheels. These wheels have two motors:
 * A drive motor that moves the robot and a turn motor that changes the wheel assembly's direction.
 * <p>
 * MOTOR LAYOUT
 * <p>
 * Front
 * Wheel 2 -------------- Wheel 1
 * |					|
 * |					|
 * |					|
 * Left 	|					|   Right
 * |					|
 * |					|
 * |					|
 * Wheel 3 -------------- Wheel 4
 * Back
 * <p>
 * The library supports two modes: Robot centric and Field centric. In Robot centric mode the robot turns relative to its
 * current position: 45 degrees to the right will turn the robot 45 degrees to the right (for example, if it is pointing
 * north before the turn, it will point north-east after the turn. In Field centric mode the robot turns to face the given
 * number of degrees relative to the firld's orientation: 0 means straight ahead down the field, 90 means to the right, etc.
 */
public class SwerveMath {
    // Robot dimensions. Units are of no importance. Required
    private final double length;
    private final double width;

    // The diagonal of the robot dimensions. Internal
    private final double diagonal;

    // The scale factor to control robot maximum speed. Optional.
    private final double SCALE_SPEED = 1;

    // The "Centric" mode for the robot
    private CentricMode centricMode = CentricMode.FIELD;

    public void setModeField() {
        centricMode = frc.team578.robot.subsystems.swerve.math.CentricMode.FIELD;
    }

    /**
     * Constructor
     *
     * @param width  the robot width (units do not matter)
     * @param length the robot length (units do not matter)
     */
    public SwerveMath(double width, double length) {
        assert (width > 0) : "Width has to be larger than 0";
        assert (length > 0) : "Length has to be larger than 0";

        this.width = width;
        this.length = length;

        diagonal = Math.sqrt(Math.pow(this.length, 2) + Math.pow(this.width, 2));
    }


    public CentricMode getCentricMode() {
        return centricMode;
    }


//    public void setCentricMode(CentricMode centricMode) {
//        this.centricMode = centricMode;
//    }


    /**
     * move
     * Moves the robot based on 3 inputs - fwd (forward), str(strafe), and rcw(rotation clockwise)
     * Inputs are between -1 and 1, with 1 being full power, -1 being full reverse, and 0 being neutral.
     * The method uses gyro for field centric driving, if it is enabled.
     *
     * @param fwd       the forward power value range -1.0(back) - 1.0(fwd)
     * @param str       the strafe power value range -1.0(left) - 1.0(right)
     * @param rcw       the rotation power value range -1.0(ccw) - 1.0(cw)
     * @param gyroValue the value of the gyro input to be used by the calculation. Optional. Only used when the robot is in field-centric mode. Values are 0-360
     * @return List of wheel movement directives. The list indices correspond to the wheel numbering scheme as above, zero-based.
     */
    public List<SwerveDirective> move(double fwd, double str, double rcw, Double gyroValue) {
        
        if (isFieldCentric()) {
            
            if (gyroValue == null) {
                throw new IllegalStateException("Cannot use field centric mode without a Gyro value");
            
            double gyro = Math.toRadians(gyroValue);
            double s = Math.sin(gyro), c = Math.cos(gyro);
            double temp = fwd * c - str * s
            str = fwd * s + str * c;
            fwd = temp;
        }

        double a = str - rcw * (length / diagonal);
        double b = str + rcw * (length / diagonal);
        double c = fwd - rcw * (width / diagonal);
        double d = fwd + rcw * (width / diagonal);
        
        Vector2D[] vecs = {new Vector2D(c, b), new Vector2D(d, b), new Vector2D(d, a), new Vector2D(c, a)};
        
        List<SwerveDirective> sd = new List<SwerveDirective>();
        double max = Vector2D.max(vecs);
        for(int i = 0; i < 4; i++)
            sd.add(new SwerveDirective(vecs[i].magnitude()/max, vecs[i].heading()));
            
        return sd;
    }
        
    private boolean isFieldCentric() {
        return centricMode.equals(CentricMode.FIELD);
    }
    
        
    class Vector2D{
        double x, y;
        
        Vector2D(double x, double y){
            this.x = x;
            this.y = y;
        }
        
        double magnitude(){ return Math.sqrt(x*x + y*y);}
        double heading(){ return Math.atan2(y, x);}
        
        // max magnitude
        static double max(Vector2D[] vecs){
            double max = vecs[0].magnitude();
            for(int i = 1; i < vecs.length; i++){
                int t = vecs[i].magnitude();
                if(max < m)
                   max = m;
            }
            return max;
        }
    }
}
