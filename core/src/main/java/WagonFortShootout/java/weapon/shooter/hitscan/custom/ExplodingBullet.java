package WagonFortShootout.java.weapon.shooter.hitscan.custom;

import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.weapon.shooter.hitscan.Hitscan;
import WagonFortShootout.java.weapon.shooter.hitscan.Explosion;
import com.badlogic.gdx.utils.JsonValue;

public class ExplodingBullet extends Hitscan {

    public final Explosion exploder;

    public ExplodingBullet(JsonValue value) {
        super(value);
        exploder = new Explosion(value.get("exploder"));
    }

    @Override
    public void onHit(HitResult data) {
        exploder.explode(data.shooter, data.hitPos, data.direction);
    }

}
