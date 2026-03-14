package WagonFortShootout.java.weapon.damager;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.weapon.damager.custom.ExplodingBullet;
import WagonFortShootout.java.weapon.damager.custom.Kinetic;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Beam implements Projectile {

    protected static final float WIDTH = 0.25f;
    protected static final int LIFETIME = 5;
    protected static final WagonFortShootout.java.framework.image.Beam BEAM = new WagonFortShootout.java.framework.image.Beam("effects/bullet", 1, 5, 0.25f);

    public float dist = 400;
    public final int damage;
    public final float weight;
    public final float SPREAD;
    public final int piercing;

    public Beam(int damage, float weight, float Spread, int piercing) {
        this.damage = damage;
        this.weight =weight;
        this.SPREAD = Spread;
        this.piercing = piercing;
    }

    public void shoot(Entity entity, Vector2 pos, float face, float addedSpread) {
        Hitbox self = entity.HITBOX;
        Mutable pierce = new Mutable(piercing);
        Vector2 direction = (Mth.addSpread(Mth.toVec(face, dist), SPREAD + addedSpread)).add(pos);
        Hitbox[] all = Utils.closetHitBox(entity);
        for (int i = 0; i < all.length; i++) {
            Hitbox hitbox = all[i];
            Vector2 eHit = new Vector2();
            if (hitbox != self && hitbox.rayIntersection(pos, direction, eHit)) {
                if(hitbox.isAnchored()) {
                    float rad = Mth.angleRad(pos, eHit);
                    onEnter(Mth.toVec(rad, pos.dst(eHit) - 0.01f).add(pos));
                }
                onHit(entity);
                hitbox.onHit(new HitData(this, pierce, entity, eHit, hitbox.entity));
                if(entity.getHealth() < 0) {
                    onKill(entity);
                }
                if (pierce.doubleValue() <= 0) {
                    direction = eHit;
                    float rad = Mth.angleRad(pos, eHit);
                    onStop(Mth.toVec(rad, pos.dst(direction) - 0.01f).add(pos));
                    break;
                } else if(hitbox.isAnchored()){
                    Vector2 oHit = new Vector2();
                    hitbox.rayIntersectionFar(pos, direction, oHit);
                    float rad = Mth.angleRad(pos, oHit);
                    onExit(Mth.toVec(rad, pos.dst(oHit) + 0.01f).add(pos));
                }
            }
        }
        BEAM.instance(pos, direction);
    }

    //When entity is hit
    public void onHit(Entity entity) {

    }

    //When entity is killed
    public void onKill(Entity entity) {

    }

    public void onStop(Vector2 pos) {

    }

    //When exiting anchored object
    public void onExit(Vector2 pos) {

    }

    //When hitting anchored object
    public void onEnter(Vector2 pos) {

    }

    public static Beam readJson(JsonValue value) {
        switch (value.getString("type")) {
            case "bullet" -> {return new Beam(value.getInt("damage"), value.getFloat("weight"), value.getFloat("spread"), value.getInt("piercing"));}
            case "kinetic" -> {return new Kinetic(value.getInt("damage"), value.getFloat("weight"), value.getFloat("spread"), value.getInt("piercing"), Explosion.readJson(value.get("exploder")));}
            case "exploding" -> {return new ExplodingBullet(value.getInt("damage"), value.getFloat("weight"), value.getFloat("spread"), value.getInt("piercing"),Explosion.readJson(value.get("exploder")));}
            default -> {throw new RuntimeException("No such projectile " + value.getString("type") + ".");}
        }
    }
}
