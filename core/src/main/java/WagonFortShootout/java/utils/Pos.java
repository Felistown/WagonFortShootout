package WagonFortShootout.java.utils;

import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.world.Hitbox;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
        Hitbox hitbox = entity.getHitbox();
        for(Hitbox other: Hitbox.getAllHitboxes()) {
            Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
            if(hitbox != other && Intersector.overlapConvexPolygons(hitbox.POLYGON, other.POLYGON, mtv)) {
                if(other.anchored) {
                    setPos(POS.cpy().add(Mth.toVec(mtv)));
                    VEL.set(0,0);
                } else {
                    setPos(POS.cpy().add(Mth.toVec(mtv).scl(0.5f)));
                }
            }
        }
    }

    public void move(Vector2 vector) {
        if(vector.len() > 0) {
            moving = true;
            VEL.add(vector.setLength(acceleration));
        }
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

    public Vector2 vel() {
        return VEL.cpy();
    }

    private void setPos(Vector2 pos) {
        POS.set(pos);
        entity.getHitbox().setPosition(POS);
        entity.getSprite().setCenter(POS.x, POS.y);
    }
}
