package frc.team578.robot.subsystems.swerve;

import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team578.robot.Robot;
import frc.team578.robot.subsystems.interfaces.UpdateDashboard;

public class FieldPosition implements UpdateDashboard{
    private static Vector2d botPos, botSpeed;
    private static TalonSwerveEnclosure[] talonEnclosures = {
        SwerveDrive.swerveEnclosureFR,
        SwerveDrive.swerveEnclosureBR,
        SwerveDrive.swerveEnclosureBL,
        SwerveDrive.swerveEnclosureFL
    };
    
    public static void init(){
        resetBotPosition();
    }

    public static void resetBotPosition(){
        botPos = new Vector2d(0, 0);
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

    public static void periodic(){  
        botSpeed = getBotSpeedVect();
    }
    // returns vector 
    public static Vector2d getBotSpeedVect(){
        Vector2d sum = vectorScale(add(getWheelState().getVecs()), .25);
        sum.rotate(Robot.gyroSubsystem.getHeading());
        return vectorScale(add(getWheelState().getVecs()), .25);
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
        return createVect(angleRadians(enclosure), enclosure.getDriveTalon().getSelectedSensorVelocity());
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

    @Override
    public void updateDashboard() {
        SmartDashboard.putNumber("X Speed vec", botSpeed.x);
        SmartDashboard.putNumber("Y Speed vec", botSpeed.y);
    }
}
