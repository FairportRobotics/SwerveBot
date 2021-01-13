package frc.team578.robot.subsystems.swerve.motionProfiling;

import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.subsystems.swerve.SwerveDrive;
import frc.team578.robot.subsystems.swerve.TalonSwerveEnclosure;
import frc.team578.robot.subsystems.swerve.WheelState;
import frc.team578.robot.Robot;


public class FieldPosition{
    private static Vector2d botPos, botSpeed;
    private static long prevTime = 0;
    private static final double LENGTH_CONSTANT = 0.0259752961; // inversly preporional to length
    private static TalonSwerveEnclosure[] talonEnclosures = {
        SwerveDrive.swerveEnclosureFR,
        SwerveDrive.swerveEnclosureBR,
        SwerveDrive.swerveEnclosureBL,
        SwerveDrive.swerveEnclosureFL
    };
    
    public static void init(){
        resetBotPosition();
        resetBotSpeed();
    }

    public static void resetBotPosition(){
        botPos = new Vector2d(0, 0);
    }

    public static void resetBotSpeed(){
        botSpeed = new Vector2d(0, 0);
    }

    public static Vector2d getBotPosition(){
        return botPos;
    }
    
    public static double getBotXPosition(){
        return botPos.x;
    }

    public static double getBotYPosition(){
        return botPos.y;
    }
    public static double getBotYSpeed(){
        return botSpeed.y;
    }
    public static double getBotXSpeed(){
        return botSpeed.x;
    }
    public static void periodic(){  
        botSpeed = getBotSpeedVect();
        long currentTime = System.currentTimeMillis();
        botPos = add(botPos, vectorScale(botSpeed, ((double)(currentTime - prevTime))/1000));
        prevTime = currentTime;
    }
    // returns speed vector in meters per second
    public static Vector2d getBotSpeedVect(){
        return vectorScale(add(getWheelState().getVecs()), .25 * LENGTH_CONSTANT);
    }

    private static WheelState getWheelState(){
        return new WheelState(
            getVectFromEnclosure(talonEnclosures[0]),
            getVectFromEnclosure(talonEnclosures[1]),
            getVectFromEnclosure(talonEnclosures[2]),
            getVectFromEnclosure(talonEnclosures[3])
        );
    }

    //returns speed vector of talon
    private static Vector2d getVectFromEnclosure(TalonSwerveEnclosure enclosure){
        return createVect(angleRadians(enclosure) - Math.toRadians(Robot.gyroSubsystem.getHeading()), enclosure.getDriveTalon().getSelectedSensorVelocity());
    }
    public static Vector2d add(Vector2d... vecs){
        double x = 0, y = 0;
        for(int i = 0; i < vecs.length; i++){
            x += vecs[i].x;
            y += vecs[i].y;
        }
        return new Vector2d(x, y); 
    }
    public static Vector2d vectorScale(Vector2d v, double scale){
        return new Vector2d(v.x*scale, v.y*scale);
    }
    private static double angleRadians(TalonSwerveEnclosure enclosure){
        return 2*Math.PI/1024*(enclosure.getSteerTalon().getSelectedSensorPosition(0)%1024);
    }
    public static Vector2d createVect(double angle, double magnitude){
        return new Vector2d(magnitude*Math.cos(angle), magnitude*Math.sin(angle));
    }

    public static void updateDashboard() {
        if(botSpeed != null && botPos != null){
            SmartDashboard.putNumber("X Speed vec", botSpeed.x);
            SmartDashboard.putNumber("Y Speed vec", botSpeed.y);
            SmartDashboard.putNumber("X pos vec", botPos.x);
            SmartDashboard.putNumber("Y pos vec", botPos.y);
        }
    }
}
