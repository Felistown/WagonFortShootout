package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.weapon.damager.Beam;
import WagonFortShootout.java.weapon.damager.Explosion;
import com.badlogic.gdx.math.Vector2;

public class ExplodingBullet extends Beam {

    public final Explosion exploder;

    public ExplodingBullet(int damage, float weight, float Spread, int piercing, Explosion exploder) {
        super(damage, weight, Spread, piercing);
        this.exploder= exploder;
    }

    public void shoot(Entity entity, Vector2 pos, float face, float addedSpread) {
        Hitbox self = entity.HITBOX;
        Mutable pierce = new Mutable(piercing);
        Vector2 direction = (Mth.addSpread(Mth.toVec(face, dist), SPREAD + addedSpread)).add(pos);
        Hitbox[] all = Utils.closetHitBox(entity);
        for (int i = 0; i < all.length; i ++) {
            Hitbox hitbox = all[i];
            Vector2 eHit = new Vector2();
            if (hitbox != self &&!(entity.mount != null && hitbox.equals(entity.mount.HITBOX)) && hitbox.rayIntersection(pos, direction, eHit)) {
                hitbox.onHit(new HitData(this, pierce, entity, eHit, hitbox.entity));
                if(pierce.doubleValue() <= 0) {
                    direction = eHit;
                    float rad = Mth.angleRad(pos, eHit);
                    exploder.explode(entity, Mth.toVec(rad, pos.dst(direction) - 0.01f).add(pos), rad);
                    break;
                }
            }
        }
        BEAM.instance(pos, direction);
    }
}
