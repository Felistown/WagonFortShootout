package WagonFortShootout.java.utils;

public class Face {

    private double facing;
    private double goal;
    private double speed;

    public Face(double facing, double speed) {
        this.facing = facing;
        this.speed = speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getFacing() {
        return facing;
    }

    public boolean isMoving() {
        return facing != goal;
    }

    public void recoil(float mult, float floor) {
        float rand = (float)Math.random() - 0.5f;
        facing += rand * mult + floor * Math.signum(rand);
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public void tick(){
        if(facing != goal) {
            double diff = goal - facing;
            if (diff > Math.PI) {
                diff -= 2 * Math.PI;
            } else if (diff < -Math.PI){
                diff += 2 * Math.PI;
            }
            if (diff >= 0) {
                if (speed >= Math.abs(diff)) {
                    facing = goal;
                } else {
                    facing += speed;
                }
            } else {
                if (speed >= Math.abs(diff)) {
                    facing = goal;
                } else {
                    facing -= speed;
                }
            }
        }
    }
}
