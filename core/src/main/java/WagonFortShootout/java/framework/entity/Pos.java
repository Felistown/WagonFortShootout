package WagonFortShootout.java.framework.entity;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.Player;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class Pos {

    private final Vector2 POS;
    private final Vector2 VEL;
    private boolean moving;
    public float acceleration;
    public float max_speed;
    private final Entity entity;

    public Pos(Vector2 pos, Entity entity) {
        POS = pos;
        VEL = new Vector2(0,0);
        this.entity = entity;
        moving = false;
        acceleration = 0.00166666666f;
        max_speed = 0.01666666666f;
    }

    public void logic() {
        setPos(POS.add(VEL));
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
        //TODO do proxy check rather than all hitboxes
        for(Hitbox other: Hitbox.getAllHitboxes()) {
            //TODO fix this nested loop structure
            Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
            if(hitbox != other && (entity.mount == null || entity.mount.HITBOX != other) && Intersector.overlapConvexPolygons(hitbox.POLYGON, other.POLYGON, mtv)) {
                if(other.anchored) {
                    setPos(POS.cpy().add(Mth.toVec(mtv)));
                } else {
                    if(entity instanceof Mount m) {
                        if(m.getMounter() != null && m.getMounter().HITBOX == other) {
                        } else {
                            setPos(POS.cpy().add(Mth.toVec(mtv).scl(0.5f)));
                        }
                    } else {
                        setPos(POS.cpy().add(Mth.toVec(mtv).scl(0.5f)));
                    }
                }
            }
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

    public Vector2 pos() {
        return POS.cpy();
    }

    public Vector2 vel() {
        return VEL.cpy();
    }

    public void setPos(Vector2 pos) {
        POS.set(Mth.clamp(pos, new Vector2(1,1), new Vector2(99,99)));
        entity.HITBOX.setPosition(POS);
        entity.getSprite().setCenter(POS.x, POS.y);
        entity.getSprite().setOriginCenter();
    }
}
