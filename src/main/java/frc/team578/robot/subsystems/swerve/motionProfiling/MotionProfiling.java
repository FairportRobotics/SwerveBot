package frc.team578.robot.subsystems.swerve.motionProfiling;

import edu.wpi.first.wpilibj.drive.Vector2d;
import frc.team578.robot.Robot;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class MotionProfiling {
    private Vector2d pos;
    private long prevTime;
    private ArrayList<Double> prevI = new ArrayList<Double>();
    private final int I_SIZE = 10;
    private long timeInit;
    private Vector2d[] botPath;
    private double timeStepMillis;
    double iTotal;

    public MotionProfiling(Vector2d pos){
        prevTime = System.currentTimeMillis();
        this.pos = pos;
        timeInit = prevTime;
        for(int j = 0; j < I_SIZE; j++)
                prevI.add(0d);

    }
    public MotionProfiling(Vector2d pos, Vector2d[] botPath, double timeStepMillis){
        this.botPath = botPath;
        prevTime = System.currentTimeMillis();
        this.pos = pos;
        this.timeStepMillis = timeStepMillis;
        timeInit = prevTime;
        for(int j = 0; j < I_SIZE; j++)
                prevI.add(0d);
    }
    public MotionProfiling(double timeStepMillis){
        readPts();
        prevTime = System.currentTimeMillis();
        this.timeStepMillis = timeStepMillis;
        timeInit = prevTime;
        pos = new Vector2d(0,0);
        for(int j = 0; j < I_SIZE; j++)
                prevI.add(0d);
    }


    public void periodic(){
        managePos();
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
        
        
        double p = .8;
        double d = .1;
        double i = 0;
        
        Vector2d power = new Vector2d(px*p + dx*d + iTotal*i/1.4142, py*p + dy*d + iTotal*i/1.4142);

        setBotPower(power);
        prevTime = time;
    }
    private void managePos(){
        if(botPath != null){
            int index = (int)((System.currentTimeMillis() - timeInit)/timeStepMillis);
            if(index < botPath.length)
                pos = botPath[index];
        }
    }
    private void setBotPower(Vector2d vec){
        Robot.swerveDriveSubsystem.swerveDriveCommand.setProfilingPowerX(vec.x);
        Robot.swerveDriveSubsystem.swerveDriveCommand.setProfilingPowerY(vec.y);
    }
    public void restartTime(){
        timeInit = System.currentTimeMillis();
    }
    public void setPos(Vector2d pos){this.pos = pos;}
    public Vector2d getPos(){return pos;}

    private void readPts(){
        botPath = new Vector2d[Points.points.length/2];
        for(int i = 0; i < Points.points.length/2; i++)
            botPath[i] = new Vector2d(Points.points[2*i], Points.points[2*i + 1]);
    }
}