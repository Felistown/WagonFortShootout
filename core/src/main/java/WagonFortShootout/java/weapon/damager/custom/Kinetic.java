package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.weapon.damager.Explosion;
import com.badlogic.gdx.math.Vector2;

public class Kinetic extends ExplodingBullet {


    public Kinetic(int damage, float weight, float Spread, int piercing, Explosion exploder) {
        super(damage, weight, Spread, piercing, exploder);
    }

    public void shoot(Entity entity, Vector2 pos, float face, float addedSpread) {
        Hitbox self = entity.HITBOX;
        Mutable pierce = new Mutable(piercing);
        Vector2 direction = (Mth.addSpread(Mth.toVec(face, dist), SPREAD + addedSpread)).add(pos);
        Hitbox[] all = Utils.closetHitBox(entity);
        for (int i = 0; i < all.length; i ++) {
            Hitbox hitbox = all[i];
            Vector2 eHit = new Vector2();
            if (hitbox != self && !(entity.mount != null && hitbox.equals(entity.mount.HITBOX))&& hitbox.rayIntersection(pos, direction, eHit)) {
                hitbox.onHit(new HitData(this, pierce, entity, eHit, hitbox.entity));
                if(hitbox.isAnchored()) {
                    Vector2 oHit = new Vector2();
                    hitbox.rayIntersectionFar(pos, direction, oHit);
                    int p = exploder.projectiles;
                    exploder.projectiles = Math.round(exploder.projectiles * ((eHit.dst(oHit) * 20 + piercing) / piercing));
                    float rad = Mth.angleRad(pos, oHit);
                    exploder.explode(entity, Mth.toVec(rad, pos.dst(oHit) + 0.01f).add(pos), rad);
                    exploder.projectiles = p;
                }
                if(pierce.doubleValue() <= 0) {
                    direction = eHit;
                    break;
                }
            }
        }
        BEAM.instance(pos, direction);
    }
}
