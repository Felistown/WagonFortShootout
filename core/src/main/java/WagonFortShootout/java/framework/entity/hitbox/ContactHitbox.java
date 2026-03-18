package WagonFortShootout.java.framework.entity.hitbox;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.utils.PolygonMaker;
import WagonFortShootout.java.utils.mutable.MutableNumber;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashSet;
import java.util.function.Consumer;

public class ContactHitbox extends Hitbox {

    private final Consumer<HitResult> onContact;
    private final int damage;
    private final int knockback;
    private final float bounciness;
    public Hitbox ignore;


    protected ContactHitbox(HitboxHolder holder, Polygon hitBox, Consumer<HitResult> onHit, Consumer<HitResult> onContact, int damage, int knockback, float bounciness) {
        super(holder, hitBox, onHit);
        this.onContact = onContact;
        this.damage =damage;
        this.knockback = knockback;
        this.bounciness = bounciness;
    }

    @Override
    public Vector2 checkCollision() {
        Vector2 mtv = new Vector2(0,0);
        Hitbox longest = null;
        Vector2 longestV = new Vector2(0,0);
        HashSet<Hitbox> hitboxes = GameLevel.spacialHash.query(this);
        for(Hitbox hitbox: hitboxes) {
            if(hitbox != this && hitbox != ignore) {
                Vector2 col = collide(hitbox);
                if(col != null) {
                    if(col.len() > longestV.len()) {
                        longest = hitbox;
                        longestV.set(col);
                    }
                    mtv.add(col);
                }
            }
        }
        if(longest != null) {
            Entity entity = ((Entity)holder);
            Vector2 oldPos = entity.getPos();
            entity.setPosAndRot(entity.getPos().add(mtv), (float)Math.toDegrees(entity.getFacing()));
            HitResult hitResult = new HitResult(damage, knockback, new MutableNumber(0), entity, oldPos, longest.holder);
            onContact.accept(hitResult);
            longest.onHit(hitResult);
        }
        ((Entity)holder).POS.addVel(mtv.cpy().scl(bounciness));
        return mtv;
    }

    public static class ContactHitboxBuilder extends Hitbox.Builder {

        private final Polygon POLYGON;
        private Consumer<HitResult> onContact;
        private final int damage;
        private final int knockback;
        private final float bounciness;

        public ContactHitboxBuilder(Polygon polygon, Consumer<HitResult> onContact, int damage, int knockback, float bounciness) {
            super(polygon);
            POLYGON = polygon;
            this.onContact = onContact;
            this.damage = damage;
            this.knockback = knockback;
            this.bounciness = bounciness;
        }

        public static Hitbox.Builder fromJson(JsonValue value, Consumer<HitResult> onContact) {
            return new ContactHitboxBuilder(PolygonMaker.fromJson(value.get("polygon")), onContact, value.getInt("damage"), value.getInt("knockback"), value.getFloat("bounciness"));

        }

        @Override
        public Hitbox build(HitboxHolder holder, Consumer<HitResult> onHit) {
            return new ContactHitbox(holder, new Polygon(POLYGON.getVertices()), onHit, onContact, damage, knockback, bounciness);
        }
    }

}
