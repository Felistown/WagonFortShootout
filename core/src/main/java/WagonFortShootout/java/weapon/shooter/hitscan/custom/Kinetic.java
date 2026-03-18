package WagonFortShootout.java.weapon.shooter.hitscan.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.weapon.shooter.hitscan.Explosion;
import WagonFortShootout.java.weapon.shooter.hitscan.Hitscan;
import com.badlogic.gdx.utils.JsonValue;

public class Kinetic extends Hitscan {

    public final Explosion exploder;

    public Kinetic(JsonValue value) {
        super(value);
        exploder = new Explosion(value.get("exploder"));
    }

    @Override
    public void onExit(HitResult data) {
        if(!(data.holder instanceof Entity e && e instanceof Mount m && m.getMounter() == data.shooter)) {
            exploder.explode(data.shooter, data.hitPos, data.direction);
        }
    }
}
