package frc.team578.robot.subsystems.swerve;

import edu.wpi.first.wpilibj.drive.Vector2d;

public class WheelState {
    public Vector2d fr, fl, br, bl;
    public WheelState(Vector2d fr, Vector2d br, Vector2d bl, Vector2d fl){
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;
    }

    public Vector2d getFl(){return fl;}
    public Vector2d getFr(){return fr;}
    public Vector2d getBl(){return bl;}
    public Vector2d getBr(){return br;}

    public Vector2d[] getVecs(){
        Vector2d[] vs = {fr, br, bl, fl};
        return vs;
    }
}