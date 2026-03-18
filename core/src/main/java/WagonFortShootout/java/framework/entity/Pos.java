package WagonFortShootout.java.framework.entity;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.annotations.KillOnWorldBounds;
import WagonFortShootout.java.framework.entity.hitbox.Hitbox;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.math.Vector2;

public class Pos {

    private final Vector2 POS;
    private final Vector2 VEL;
    private boolean moving;
    public float acceleration;
    public float max_speed;
    private final Entity entity;

    public Pos(Vector2 pos, Entity entity, float max_speed, float acceleration) {
        POS = pos;
        VEL = new Vector2(0,0);
        this.entity = entity;
        moving = false;
        this.acceleration = acceleration;
        this.max_speed = max_speed;
    }

    public void logic() {
        POS.add(VEL);
        collision();
        if(!moving || VEL.len() > max_speed) {
            if(VEL.len() < acceleration) {
                VEL.setLength(0);
            } else {
                VEL.setLength(VEL.len() - acceleration);
            }
        } else {
            moving = false;
        }
    }

    public void collision() {
        Hitbox hitbox = entity.HITBOX;
        POS.set(POS.cpy().add(hitbox.checkCollision()));
        if(Mth.clamp(POS) && entity.getClass().isAnnotationPresent(KillOnWorldBounds.class)) {
            entity.health = 0;
        }
    }

    public void move(Vector2 vector) {
        if(vector.len() > 0) {
            moving = true;
            VEL.add(vector.setLength(Math.min(acceleration, vector.len())));
        }
    }

    public void addVel(Vector2 other) {
        VEL.add(other);
    }

    public void addVel(float direction, float magnitude) {
        VEL.add(new Vector2((float)-Math.cos(direction) * magnitude, (float)-Math.sin(direction) * magnitude));
    }

    public void setVel(Vector2 vel) {
        VEL.set(vel);
    }

    public Vector2 pos() {
        return POS.cpy();
    }

    public void setPos(Vector2 pos) {
        POS.set(pos);
    }

    public Vector2 vel() {
        return VEL.cpy();
    }
}
