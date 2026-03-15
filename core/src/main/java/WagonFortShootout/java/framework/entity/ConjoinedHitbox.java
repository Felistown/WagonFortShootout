package WagonFortShootout.java.framework.entity;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class ConjoinedHitbox extends Hitbox{

    private final HashSet<SubHitbox> subHitboxes = new HashSet<SubHitbox>();

    public ConjoinedHitbox(HitboxHolder holder, Polygon master, Polygon[] sub, Vector2[] offset, Consumer<HitResult> onHit, ArrayList<Consumer<HitResult>> onHits) {
        super(holder, master, onHit);
        if(sub.length != offset.length) {
            throw new IllegalArgumentException("Subordinate length must be the same as offset length, found " + sub.length + " of expected " + offset.length + ".");
        } else if(sub.length != onHits.size()) {
            throw new IllegalArgumentException("Subordinate length must be the same as behaviour length, found " + sub.length + " of expected " + onHits.size() + ".");
        }
        for(int i = 0; i < sub.length; i++) {
            Consumer<HitResult> behaviour = onHits.get(i);
            if(behaviour == null) {
                subHitboxes.add(new SubHitbox(sub[i], offset[i], onHit));
            } else {
                subHitboxes.add(new SubHitbox(sub[i], offset[i], behaviour));
            }
        }
    }

    @Override
    public void setPosition(Vector2 pos) {
        super.setPosition(pos);
        subHitboxes.forEach(e -> e.innerSetPosition(pos));
    }

    @Override
    public void setRotation(float angleDeg) {
        super.setRotation(angleDeg);
        subHitboxes.forEach(e -> e.innerSetRotation(angleDeg));
    }

    @Override
    public void remove() {
        subHitboxes.forEach(SubHitbox::innerRemove);
        super.remove();
    }

    @Override
    public void display() {
        super.display();
        subHitboxes.forEach(SubHitbox::innerDisplay);
    }

    public Hitbox[] getSubs() {
        return subHitboxes.toArray(new Hitbox[]{});
    }

    @Override
    public boolean collide(Hitbox other, Intersector.MinimumTranslationVector mtv) {
        if(other instanceof SubHitbox s && subHitboxes.contains(s)) {
            return false;
        }
        if(super.collide(other, mtv)) {
            return true;
        } else {
            for(SubHitbox sh : subHitboxes) {
                if(sh.collide(other, mtv)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setAnchored(Boolean anchored) {
        subHitboxes.forEach(e -> e.innerAnchored(anchored));
        super.setAnchored(anchored);
    }

    @Override
    public void setTransparent(Boolean transparent) {
        subHitboxes.forEach(e -> e.innerTransparent(transparent));
        super.setTransparent(transparent);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
           return false;
        } else if(this == obj) {
            return true;
        } else {
            for(SubHitbox s: subHitboxes) {
                if(s == obj) {
                    return true;
                }
            }
            return false;
        }
    }

    public class SubHitbox extends Hitbox{

        private final Vector2 offset;

        public SubHitbox(Polygon hitBox, Vector2 offset,Consumer<HitResult> onHit) {
            super(ConjoinedHitbox.this.holder, hitBox, onHit);
            this.offset = offset;
        }

        private void innerTransparent(Boolean t) {
            super.setTransparent(t);
        }

        private void innerAnchored(Boolean t) {
            super.setAnchored(t);
        }

        private void innerSetPosition(Vector2 pos) {
            super.setPosition(pos.cpy().add(offset.cpy().rotateDeg(ConjoinedHitbox.this.getRotation())));
        }

        private void innerSetRotation(float angleDeg) {
            super.setRotation(angleDeg);
        }

        private void innerRemove() {
            super.remove();
        }

        private void innerDisplay() {
            super.display();
        }

        @Override
        public void setPosition(Vector2 pos) {
            ConjoinedHitbox.this.setPosition(pos);
        }

        @Override
        public void setRotation(float angleDeg) {
            ConjoinedHitbox.this.setRotation(angleDeg);
        }

        @Override
        public void remove() {
            ConjoinedHitbox.this.remove();
        }

        @Override
        public void display() {
            ConjoinedHitbox.this.display();
        }

        @Override
        public void setAnchored(Boolean anchored) {
            ConjoinedHitbox.this.setAnchored(anchored);
        }

        @Override
        public void setTransparent(Boolean transparent) {
            ConjoinedHitbox.this.setTransparent(transparent);
        }

        @Override
        public boolean equals(Object obj) {
            return ConjoinedHitbox.this.equals(obj);
        }
    }
}
