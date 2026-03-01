package WagonFortShootout.java.utils;

import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.world.Hitbox;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Pos {

    private final Vector2 POS;
    private final Vector2 VEL;
    private final Entity entity;
    private final float DECEL = 0.1f;

    public Pos(Vector2 pos, Entity entity) {
        POS = pos;
        VEL = new Vector2(0,0);
        this.entity = entity;
    }

    public void logic() {
        setPos(POS.add(VEL));
        Hitbox hitbox = entity.getHitbox();
        for(Hitbox other: Hitbox.getAllHitboxes()) {
            Vector2 hitPos = new Vector2();
            if(hitbox != other && hitbox.hitBoxIntersection(other, hitPos)) {
                if(other.anchored) {
                    if(Intersector.isPointInPolygon(new Array<Vector2>(hitbox.getVertices()), hitPos)) {
                        setPos(POS.cpy().add(POS.cpy().sub(hitPos)));
                        VEL.set(0,0);
                    }
                } else {
                    setPos(POS.cpy().add(POS.cpy().sub(hitPos).scl(0.1f)));
                }
            }
        }
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

    private void setPos(Vector2 pos) {
        POS.set(pos);
        entity.getHitbox().setPosition(POS);
        entity.getSprite().setCenter(POS.x, POS.y);
    }
}
