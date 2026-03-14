package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
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

    @Override
    public void onHit(HitResult data) {
        exploder.explode(data.shooter, data.hitPos, data.direction);
    }

}
