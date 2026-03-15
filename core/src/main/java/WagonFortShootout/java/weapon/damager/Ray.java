package WagonFortShootout.java.weapon.damager;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Beam;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Ray implements Projectile {

    protected static final Beam BEAM = new WagonFortShootout.java.framework.image.Beam("effects/bullet", 1, 5, 0.25f);

    public final float dist;
    public final int damage;
    public final float weight;
    public final float SPREAD;
    public final int piercing;
    public final Beam beam;

    public Ray(JsonValue value) {
        damage = value.getInt("damage") ;
        weight = value.getFloat("weight") ;
        SPREAD = value.getFloat("spread");
        piercing = value.getInt("piercing");
        dist = value.getFloat("dist");
        JsonValue texture = value.get("texture");
        if(texture.type() ==  JsonValue.ValueType.stringValue && texture.asString().equals("default")) {
            beam = BEAM;
        } else {
            beam = Beam.fromJson(texture);
        }
    }

    public void shoot(Entity entity, Vector2 pos, float face, float addedSpread) {
        //TODO clean up this class
        Hitbox self = entity.HITBOX;
        Mutable pierce = new Mutable(piercing);
        Vector2 direction = (Mth.addSpread(Mth.toVec(face, dist), SPREAD + addedSpread)).add(pos);
        Hitbox[] all = Utils.closetHitBox(entity);
        for (int i = 0; i < all.length; i++) {
            Hitbox hitbox = all[i];
            Vector2 eHit = new Vector2();
            if (hitbox != self && hitbox.rayIntersection(pos, direction, eHit)) {
                //TODO things sometimes explode inside
                if(hitbox.isAnchored()) {
                    float rad = Mth.angleRad(pos, eHit);
                    onEnter(new HitResult(this, pierce, entity, Mth.toVec(rad, pos.dst(eHit) - 0.01f).add(pos), hitbox.holder));
                }
                onHit(new HitResult(this, pierce, entity, eHit, hitbox.holder));
                hitbox.onHit(new HitResult(this, pierce, entity, eHit, hitbox.holder));
                if(hitbox.holder instanceof Entity e && e.getHealth() < 0) {
                    onKill(new HitResult(this, pierce, entity, eHit, hitbox.holder));
                }
                if (pierce.doubleValue() <= 0) {
                    direction = eHit;
                    float rad = Mth.angleRad(pos, eHit);
                    onStop(new HitResult(this, pierce, entity, Mth.toVec(rad, pos.dst(direction) - 0.01f).add(pos), hitbox.holder));
                    break;
                } else if(hitbox.isAnchored()){
                    Vector2 oHit = new Vector2();
                    hitbox.rayIntersectionFar(pos, direction, oHit);
                    float rad = Mth.angleRad(pos, oHit);
                    onExit(new HitResult(this, pierce, entity, Mth.toVec(rad, pos.dst(oHit) + 0.01f).add(pos), hitbox.holder));
                }
            }
        }
        beam.instance(pos, direction);
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
