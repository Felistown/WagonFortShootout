package WagonFortShootout.java.weapon.shooter.hitscan;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.entity.hitbox.Hitbox;
import WagonFortShootout.java.framework.image.Beam;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.MutableNumber;
import WagonFortShootout.java.weapon.shooter.Shootable;
import WagonFortShootout.java.world.RayCast;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Hitscan implements Shootable {

    protected static final Beam BEAM = new WagonFortShootout.java.framework.image.Beam("effects/bullet", 1, 5, 0.25f);

    public final float dist;
    public final int damage;
    public final float weight;
    public final float SPREAD;
    public final int piercing;
    public final Beam beam;

    public Hitscan(JsonValue value) {
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
        MutableNumber pierce = new MutableNumber(piercing);
        Vector2 direction = (Mth.addSpread(Mth.toVec(face, dist), SPREAD + addedSpread)).add(pos);
        RayCast rayCast = new RayCast(pos, direction);
        rayCast.enterAndExit((enter, exit) -> {
            Hitbox hitbox = enter.hitbox();
            float angle = enter.angleRad();
            if (!(enter.hitbox().holder instanceof Entity e) || e != entity) {
                if (hitbox.isAnchored()) {
                    onEnter(new HitResult(this, pierce, entity, Mth.toVec(angle, pos.dst(enter.pos()) - 0.01f).add(pos), hitbox.holder));
                }
                onHit(new HitResult(this, pierce, entity, enter.pos(), hitbox.holder));
                hitbox.onHit(new HitResult(this, pierce, entity, enter.pos(), hitbox.holder));
                if (hitbox.holder instanceof Entity e && e.getHealth() < 0) {
                    onKill(new HitResult(this, pierce, entity, enter.pos(), hitbox.holder));
                }
                if (pierce.doubleValue() <= 0) {
                    onStop(new HitResult(this, pierce, entity, Mth.toVec(angle, pos.dst(enter.pos()) - 0.01f).add(pos), hitbox.holder));
                    direction.set(enter.pos());
                    return false;
                } else if (hitbox.isAnchored()) {
                    onExit(new HitResult(this, pierce, entity, Mth.toVec(angle, pos.dst(exit.pos()) + 0.01f).add(pos), hitbox.holder));
                }
            }
            return true;
        });
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
