package WagonFortShootout.java.framework.entity;

import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.image.Beam;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class Hitbox {

    private static final HashSet<Hitbox> ALL_HITBOXES = new HashSet<Hitbox>();

    public final Polygon POLYGON;
    private boolean anchored;
    private boolean transparent;
    private final Consumer<HitData> onHit;

    protected Hitbox(Polygon hitBox, Consumer<HitData> onHit) {
        POLYGON = hitBox;
        anchored = false;
        ALL_HITBOXES.add(this);
        this.onHit = onHit;
        transparent = false;
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
        for (int i = 0; i < verticies.length; i++) {
            Vector2 current = verticies[i];
            Beam.DEBUG_BEAM.instance(last, current);
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

    public float getRotation() {
        return POLYGON.getRotation();
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

    public boolean isAnchored() {
        return anchored;
    }

    public void setAnchored(Boolean anchored) {
        this.anchored = anchored;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(Boolean transparent) {
        this.transparent = transparent;
    }

    public boolean collide(Hitbox other, Intersector.MinimumTranslationVector mtv) {
        return Intersector.overlapConvexPolygons(POLYGON, other.POLYGON, mtv);
    }

    @Override
    public boolean equals(Object obj) {
        //TODO remove instances whereby [==] is used instead of .equals()
        if(obj == null) {
            return false;
        }
        return super.equals(obj);
    }

    public static class Builder {

        private final Polygon POLYGON;
        private final ArrayList<Polygon> sub = new ArrayList<Polygon>();
        private final ArrayList<Vector2> offset = new ArrayList<Vector2>();

        private Builder(Polygon polygon) {
            POLYGON = polygon;
        }

        public static Builder polygon(Polygon polygon) {
            return new Builder(polygon);
        }

        public boolean collide(Hitbox other, Intersector.MinimumTranslationVector mtv) {
            return Intersector.overlapConvexPolygons(POLYGON, other.POLYGON, mtv);
        }

        public static Builder empty() {
            return new Builder(new Polygon(new float[]{0,0,0,0,0,0}));
        }

        public static Builder rectangle(float width, float height) {
            return new Builder(Mth.rectange(width, height));
        }

        public static Builder circle(float radius, int sides) {
            return new Builder(Mth.circle(radius, sides));
        }

        public static Builder triangle(float width, float height) {
            return new Builder(Mth.triangle(width, height));
        }

        public Builder addSub(Polygon polygon, Vector2 offset) {
            sub.add(polygon);
            this.offset.add(offset);
            return this;
        }

        public Hitbox build(Consumer<HitData> onHit) {
            if(!sub.isEmpty()) {
                return new ConjoinedHitbox(POLYGON, sub.toArray(Polygon[]::new), offset.toArray(Vector2[]::new), onHit);
            } else {
                return new Hitbox(POLYGON, onHit);
            }
        }

    }
}
