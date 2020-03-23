package frc.team578.robot.subsystems.swerve.motionProfiling;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.drive.Vector2d;
import frc.team578.robot.Robot;
import java.util.ArrayList;

public class MotionProfiling {
    private double[] pathIn = Points.getTotalPoints();
    private Vector2d pos;
    private long prevTime;
    private ArrayList<Double> prevI = new ArrayList<Double>();
    private final int I_SIZE = 10;
    private long timeInit;
    private Vector2d[] botPath;
    private double[] botRot;
    private double timeStepMillis, angle;
    private Points.TimedCommand[] commands;
    private int commInd;
    double iTotal;
    private static CommandGroup commGroup = new CommandGroup();

    public MotionProfiling(Vector2d pos){
        angle = 0;
        prevTime = System.currentTimeMillis();
        this.pos = pos;
        timeInit = prevTime;
        for(int j = 0; j < I_SIZE; j++)
                prevI.add(0d);

    }
    public MotionProfiling(){
        angle = 0;
        readPts();
        prevTime = System.currentTimeMillis();
        this.timeStepMillis = (long)Points.curvesPerSec*Points.pointsPerCurve;
        timeInit = prevTime;
        commands = Points.commands;
        pos = new Vector2d(0,0);
        commInd = 0;
        for(int j = 0; j < I_SIZE; j++)
                prevI.add(0d);
    }


    public void periodic(){
        if(botPath != null){
            int ind = (int)((System.currentTimeMillis() - timeInit)/timeStepMillis);
            managePos(ind);
            manageAngle(ind);
            manageCommands();
        }
        long time = System.currentTimeMillis();
        double botX = FieldPosition.getBotXPosition();
        double botY = FieldPosition.getBotYPosition();

        double px = botX - pos.x;
        double py = botY - pos.y;
        double il = (Math.abs(py) + Math.abs(px))*(time-prevTime);
        prevI.add(il);
        iTotal = il - prevI.remove(I_SIZE);
        double dx = FieldPosition.getBotXSpeed();
        double dy = FieldPosition.getBotYSpeed();
       
        
        double p = Points.pidValues[0];
        double i = 0;  // not functional
        double d = Points.pidValues[2];

        double dl = d*Math.sqrt(dx*dx + dy*dy);
        
        //Vector2d power = new Vector2d(px*p + dx*d + iTotal*i/1.4142, py*p + dy*d + iTotal*i/1.4142);
        Vector2d power = new Vector2d(px*p + iTotal*i/1.4142, py*p + iTotal*i/1.4142);
        double a = Math.atan2(power.y, power.x);
        
        double anglePower =  Math.toRadians(Robot.gyroSubsystem.getHeading()) - angle;
        anglePower %= 2*Math.PI;
        if(Math.abs(anglePower) > Math.PI)
            anglePower += 2*Math.PI*(anglePower<0? 1: -1);
        if(Math.abs(anglePower) > 1)
            anglePower = (anglePower<0? -1: 1);
        
        setBotPower(new Vector2d(power.x + dl*Math.cos(a), power.y + dl*Math.sin(a)), anglePower);
        prevTime = time;
    }
    private void managePos(int ind){
        if(ind < botPath.length)
            pos = botPath[ind];
    }
    private void manageAngle(int ind){
        if(ind < botPath.length)
            angle = botRot[ind];
    }
    private void manageCommands(){
        if(commInd != commands.length && System.currentTimeMillis() > commands[commInd].getT()){
            try{
                commGroup.addSequential((Command)Class.forName("frc.team578.robot.commands." + commands[commInd].getName()).getDeclaredConstructor().newInstance());
                commInd++;
            }catch (Exception e){
                System.err.println("Command class " + commands[commInd].getName() + " does not exist");
            }
        }
    }
    private void setBotPower(Vector2d vec, double angle){
        Robot.swerveDriveSubsystem.swerveDriveCommand.setProfilingPowerX(vec.x);
        Robot.swerveDriveSubsystem.swerveDriveCommand.setProfilingPowerY(vec.y);
        Robot.swerveDriveSubsystem.swerveDriveCommand.setProfilingPowerA(angle);
    }
    public void restartTime(){
        timeInit = System.currentTimeMillis();
        commInd = 0;
    }
    public void setPos(Vector2d pos){this.pos = pos;}
    public Vector2d getPos(){return pos;}

    private void readPts(){
        botPath = new Vector2d[pathIn.length/3];
        botRot = new double[pathIn.length/3];
        for(int i = 0; i < pathIn.length/3; i++){
            botPath[i] = new Vector2d(pathIn[3*i], pathIn[3*i + 1]);
            botRot[i] = pathIn[3*i + 2];
        }
    }
}