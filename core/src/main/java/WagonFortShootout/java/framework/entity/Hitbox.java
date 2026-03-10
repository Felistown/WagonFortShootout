package WagonFortShootout.java.framework.entity;

import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;

import java.util.HashSet;
import java.util.function.Consumer;

public class Hitbox {

    private static final HashSet<Hitbox> ALL_HITBOXES = new HashSet<Hitbox>();

    public final Polygon POLYGON;
    public boolean anchored;
    private final Consumer<HitData> onHit;

    public Hitbox(Polygon hitBox, Consumer<HitData> onHit) {
        //TODO add conjoined hitbox to make more complex shapes
        POLYGON = hitBox;
        anchored = false;
        ALL_HITBOXES.add(this);
        this.onHit = onHit;
    }

    public Vector2[] getVertices() {
        float[] v = POLYGON.getTransformedVertices();
        Vector2[] vertices = new Vector2[v.length / 2];
        int index = 0;
        for(int i = 0; i + 1 < v.length; i += 2) {
            vertices[index] = new Vector2(v[i], v[i + 1]);
            //Effect.addEffect(new Effect(new Texture("image/missing_texture.png"),0.2f,0.2f), 1000, vertices[index], 0,4);
            index++;
        }
        return vertices;
    }

    public void display() {
        Vector2[] verticies = getVertices();
        Vector2 last = verticies[verticies.length - 1];
        for(int i = 0; i < verticies.length; i++) {
            Vector2 current = verticies[i];
            Beam.beam(last, current, 0.25f, 1, Color.BLACK);
            last = current;
        }
    }

    public boolean rayIntersection(Vector2 from, Vector2 to, Vector2 pos) {
        return Mth.intersectSegmentPolygon(from, to, POLYGON, pos);
    }

    public boolean rayIntersectionFar(Vector2 from, Vector2 to, Vector2 pos) {
        return Mth.intersectSegmentPolygonFar(from, to, POLYGON, pos);
    }

    public boolean hitBoxIntersection(Hitbox other, Vector2 pos) {
        Polygon overlap = new Polygon();
        boolean intersect = Intersector.intersectPolygons(other.POLYGON, POLYGON, overlap);
        if(intersect) {
            overlap.getCentroid(pos);
        }
        return intersect;
    }

    public void setPosition(Vector2 pos) {
        POLYGON.setPosition(pos.x, pos.y);
    }

    public Vector2 getPosition() {
        return POLYGON.getCentroid(new Vector2());
    }

    public void setRotation(float angleDeg) {
        POLYGON.setRotation(angleDeg);
    }

    public void remove() {
        ALL_HITBOXES.remove(this);
    }

    public static Hitbox[] getAllHitboxes() {
        return ALL_HITBOXES.toArray(Hitbox[]::new);
    }

    public void onHit(HitData data) {
        if(onHit != null) {
            onHit.accept(data);
        }
    }

    public boolean pointIntersects(Hitbox hitbox, Vector2 pos) {
        Polygon copy = new Polygon(hitbox.POLYGON.getVertices());
        copy.setPosition(pos.x, pos.y);
        return Intersector.intersectPolygons(copy, POLYGON, null);
    }

    public boolean traverable(Vector2 pos) {
        if(!Mth.within(pos, new Vector2(1,1), new Vector2(99,99))) {
            return false;
        }
        Polygon copy = new Polygon(POLYGON.getVertices());
        copy.setPosition(pos.x , pos.y);
        for(Hitbox h: Hitbox.getAllHitboxes()) {
            if(h != this && Intersector.intersectPolygons(copy, h.POLYGON,null)) {
                return false;
            }
        }
        return true;
    }
}
