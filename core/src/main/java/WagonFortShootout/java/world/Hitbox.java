package WagonFortShootout.java.world;

import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.math.*;

import java.util.HashSet;

public class Hitbox {

    private static final HashSet<Hitbox> ALL_HITBOXES = new HashSet<Hitbox>();

    public final Polygon POLYGON;
    public boolean anchored;
    public final int RESISTANCE;

    public Hitbox(Polygon hitBox, Vector2 pos, int resistance) {
        POLYGON = hitBox;
        hitBox.setPosition(pos.x, pos.y);
        anchored = false;
        this.RESISTANCE = resistance;
        ALL_HITBOXES.add(this);
    }

    public static Hitbox circle(Vector2 pos, int resistance, float radius, int sides) {
        return new Hitbox(Mth.circle(radius, sides), pos, resistance);
    }

    public static Hitbox rectange(Vector2 pos, int resistance,float length, float height) {
        return new Hitbox(Mth.rectange(length, height), pos, resistance);
    }

    public static Hitbox triangle(Vector2 pos, int resistance,float length, float height) {
        return new Hitbox(Mth.triangle(length, height), pos, resistance);
    }

    public Vector2[] getVertices() {
        float[] v = POLYGON.getTransformedVertices();
        Vector2[] vertices = new Vector2[v.length / 2];
        int index = 0;
        for(int i = 0; i + 1 < v.length; i += 2) {
            vertices[index] = new Vector2(v[i], v[i + 1]);
            //Effect.addEffect(new Effect(new Texture("image/missing_texture.png"),0.2f,0.2f), 1000, vertices[index], 0);
            index++;
        }
        return vertices;
    }

    public boolean rayIntersection(Vector2 from, Vector2 to, Vector2 pos) {
        return Mth.intersectSegmentPolygon(from, to, POLYGON, pos);
    }

    public boolean hitBoxIntersection(Hitbox other, Vector2 pos) {
        Polygon overlap = new Polygon();
        boolean intersect = Intersector.intersectPolygons(other.POLYGON, POLYGON, overlap);
        if(intersect) {
            pos.set(overlap.getCentroid(pos));
        }
        return intersect;
    }

    public void setPosition(Vector2 pos) {
        POLYGON.setPosition(pos.x, pos.y);
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
}
