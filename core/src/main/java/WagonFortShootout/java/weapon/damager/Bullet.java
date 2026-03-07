package WagonFortShootout.java.weapon.damager;

import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Bullet{

    protected static final float WIDTH = 0.25f;
    protected static final int LIFETIME = 5;
    protected static final Color COLOUR = new Color(255, 214, 0, 1);

    public float dist = 400;
    public final int damage;
    public final float weight;
    public final float SPREAD;
    public final int piercing;

    public Bullet(int damage, float weight, float Spread, int piercing) {
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
                hitbox.onHit(new HitData(this, pierce, entity, entity.getPos()));
                if (pierce.doubleValue() <= 0) {
                    direction = eHit;
                    break;
                }
            }
        }
        Beam.beam(pos, direction, WIDTH, LIFETIME, COLOUR);
    }

    public static Bullet readJson(JsonValue value) {
        switch (value.getString("type")) {
            case "bullet" -> {return new Bullet(value.getInt("damage"), value.getFloat("weight"), value.getFloat("spread"), value.getInt("piercing"));}
            case "kinetic" -> {return new Kinetic(value.getInt("damage"), value.getFloat("weight"), value.getFloat("spread"), value.getInt("piercing"), Explosion.readJson(value.get("exploder")));}
            case "exploding" -> {return new ExplodingBullet(value.getInt("damage"), value.getFloat("weight"), value.getFloat("spread"), value.getInt("piercing"),Explosion.readJson(value.get("exploder")));}
            default -> {throw new RuntimeException("No such projectile " + value.getString("type") + ".");}
        }
    }
}
