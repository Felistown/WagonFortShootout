package WagonFortShootout.java.utils;

import WagonFortShootout.java.GameLevel;
import com.badlogic.gdx.math.Vector2;

public class Pos {

    private final Vector2 POS;
    private final Vector2 VEL;
    private final float DECEL = 0.1f;

    public Pos(Vector2 pos) {
        POS = pos;
        VEL = new Vector2(0,0);

    }

    public void logic() {
        POS.add(VEL);
        VEL.x -= Mth.sigmin(VEL.x * Math.signum(VEL.x), DECEL * Math.signum(VEL.x));
        VEL.y -= Mth.sigmin(VEL.y * Math.signum(VEL.y),DECEL * Math.signum(VEL.y));
    }


    public void addVel(Vector2 other) {
        VEL.add(other);
    }

    public void addVel(float direction, float magnitud) {
        VEL.add(new Vector2((float)-Math.cos(direction) * magnitud, (float)-Math.sin(direction) * magnitud));
    }

    public Vector2 pos() {
        return POS.cpy();
    }
}
