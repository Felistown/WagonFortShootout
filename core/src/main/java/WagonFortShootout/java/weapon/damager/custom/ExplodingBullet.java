package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.weapon.damager.Ray;
import WagonFortShootout.java.weapon.damager.Explosion;
import com.badlogic.gdx.utils.JsonValue;

public class ExplodingBullet extends Ray {

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
