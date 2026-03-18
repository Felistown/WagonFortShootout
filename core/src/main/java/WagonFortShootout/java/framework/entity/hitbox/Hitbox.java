package WagonFortShootout.java.framework.entity.hitbox;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.image.Beam;
import WagonFortShootout.java.utils.GridBounds;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.PolygonMaker;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class Hitbox {
    //TODO the way that rays are cast and collisions are detected is too inefficient, implement a spacial hash
    private static final HashSet<Hitbox> ALL_HITBOXES = new HashSet<Hitbox>();

    public final HitboxHolder holder;
    public final Polygon POLYGON;
    private boolean anchored;
    private boolean transparent;
    private boolean collidable;
    private final Consumer<HitResult> onHit;
    public GridBounds gridBounds;

    protected Hitbox(HitboxHolder holder, Polygon hitBox, Consumer<HitResult> onHit) {
        this.holder = holder;
        POLYGON = hitBox;
        anchored = false;
        ALL_HITBOXES.add(this);
        this.onHit = onHit;
        transparent = false;
        collidable = true;
        gridBounds = new GridBounds();
        GameLevel.spacialHash.add(this);
    }

    public Vector2[] getVertices() {
        float[] v = POLYGON.getTransformedVertices();
        Vector2[] vertices = new Vector2[v.length / 2];
        int index = 0;
        for(int i = 0; i + 1 < v.length; i += 2) {
            vertices[index] = new Vector2(v[i], v[i + 1]);
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

    public Rectangle getBoundingBox() {
        return POLYGON.getBoundingRectangle();
    }

    public boolean rayIntersection(Vector2 from, Vector2 to, Vector2 pos) {
        return Mth.intersectSegmentPolygon(from, to, POLYGON, pos);
    }

    public boolean rayIntersectionFar(Vector2 from, Vector2 to, Vector2 pos) {
        return Mth.intersectSegmentPolygonFar(from, to, POLYGON, pos);
    }

    public void setPosAndRot(Vector2 pos, float deg) {
        if(!getPosition().equals(pos) || getRotation() != deg) {
            POLYGON.setPosition(pos.x, pos.y);
            POLYGON.setRotation(deg);
            GameLevel.spacialHash.update(this);
        }
    }

    public Vector2 getPosition() {
        return new Vector2(POLYGON.getX(), POLYGON.getY());
    }

    public float getRotation() {
        return POLYGON.getRotation();
    }

    public void remove() {
        ALL_HITBOXES.remove(this);
        GameLevel.spacialHash.remove(this);
    }

    public static Hitbox[] getAllHitboxes() {
        return ALL_HITBOXES.toArray(Hitbox[]::new);
    }

    public void onHit(HitResult data) {
        if(onHit != null) {
            onHit.accept(data);
        }
    }

    public Vector2 checkCollision() {
        if(!isCollidable()) {
            return new Vector2();
        }
        Vector2 mtv = new Vector2(0,0);
        HashSet<Hitbox> hitboxes = GameLevel.spacialHash.query(this);
        for(Hitbox hitbox: hitboxes) {
            if(hitbox != this) {
                Vector2 col = collide(hitbox);
                if(col != null) {
                    mtv.add(col);
                }
            }
        }
        return mtv;
    }

    public boolean pointIntersects(Hitbox hitbox, Vector2 pos) {
        if(!hitbox.isCollidable()) {
            return true;
        }
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
        Rectangle bounds = copy.getBoundingRectangle();
        for(Hitbox h: GameLevel.spacialHash.query(pos, bounds.getWidth(), bounds.getHeight())) {
            if(h != this && h.isCollidable() && Intersector.intersectPolygons(copy, h.POLYGON,null)) {
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

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(Boolean collidable) {
        this.collidable = collidable;
    }

    protected Vector2 collide(Hitbox other) {
        if(!other.isCollidable()) {
            return new Vector2();
        }
        Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
        if(other == this || !Intersector.overlapConvexPolygons(POLYGON, other.POLYGON, mtv)) {
            return null;
        } else {
            if(!other.isAnchored()) {
                mtv.depth /= 2;
            }
            return Mth.toVec(mtv);
        }
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
        private final ArrayList<Consumer<HitResult>> behaviour = new ArrayList<Consumer<HitResult>>();

        protected Builder(Polygon polygon) {
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
            behaviour.add(null);
            return this;
        }

        public Builder addSub(Polygon polygon, Vector2 offset, Consumer<HitResult> behaviour) {
            sub.add(polygon);
            this.offset.add(offset);
            this.behaviour.add(behaviour);
            return this;
        }

        public Builder addSubNoOp(Polygon polygon, Vector2 offset) {
            sub.add(polygon);
            this.offset.add(offset);
            behaviour.add(HitResult.NO_OP);
            return this;
        }

        public Hitbox build(HitboxHolder holder, Consumer<HitResult> onHit) {
            if(!sub.isEmpty()) {
                return new ConjoinedHitbox(holder, POLYGON, sub.toArray(Polygon[]::new), offset.toArray(Vector2[]::new), onHit, behaviour);
            } else {
                return new Hitbox(holder, POLYGON, onHit);
            }
        }

        public static Hitbox.Builder fromJson(JsonValue value) {
            JsonValue first = value.child;
            Builder builder = new Builder(PolygonMaker.fromJson(first));
            for(JsonValue entry = first.next; entry != null; entry = entry.next) {
                builder.addSub(PolygonMaker.fromJson(entry.get("polygon")), Mth.jsonToVec(entry.get("offset")));
            }
            return builder;
        }

    }
}
