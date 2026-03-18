package WagonFortShootout.java.weapon.shooter.projectile.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.weapon.shooter.hitscan.Explosion;
import WagonFortShootout.java.weapon.shooter.projectile.Projectile;
import com.badlogic.gdx.utils.JsonValue;

public class RocketHe extends Projectile {

    public final Explosion exploder;

    public RocketHe(JsonValue value) {
        super(value);
        exploder = new Explosion(value.get("exploder"));
    }

    @Override
    public void onHit(HitResult data) {
        exploder.explode(data.shooter, data.shooter.getPos(), data.direction);
        data.shooter.health = 0;
    }

}
