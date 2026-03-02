package WagonFortShootout.java.utils;

import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.world.Hitbox;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.function.Consumer;

public class Pos {

    private final Vector2 POS;
    private final Vector2 VEL;
    private final Entity entity;
    private final float DECEL = 0.1f;
    public Vector2 pos;

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
                        //setPos(POS.cpy().add(POS.cpy().sub(hitPos).scl(1)));
                        //TODO fix this collision
                        Polygon p = new Polygon();
                        Intersector.intersectPolygons(hitbox.POLYGON, other.POLYGON, p);
                        float plen = Mth.lengthIntersect(hitbox.getPosition(), other.getPosition(), p);
                        float rad = (float)Math.PI + (float)Math.atan((hitbox.POLYGON.getY() - hitPos.y) / (hitbox.POLYGON.getX() - hitPos.x));
                        //System.out.println(Math.atan((hitbox.POLYGON.getY() - hitPos.y) / (hitbox.POLYGON.getX() - hitPos.x)) + " " + rad   );
                        //System.out.println(hitPos.angleRad(hitbox.getPosition()));
                        Effect.addEffect(new Effect(new Texture("image/missing_texture.png"),0.5f,0.5f), 1000, POS.cpy().add(Mth.toVec(rad, plen)), 0);
                        setPos(POS.cpy().add(Mth.toVec(rad, plen)));
                        VEL.set(0,0);
                    }
                } else {
                    setPos(POS.cpy().add(POS.cpy().sub(hitPos).scl(0.01f)));
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

    public Vector2 vel() {
        return VEL.cpy();
    }

    private void setPos(Vector2 pos) {
        POS.set(pos);
        entity.getHitbox().setPosition(POS);
        entity.getSprite().setCenter(POS.x, POS.y);
    }
}
