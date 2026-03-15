package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.weapon.damager.Ray;
import WagonFortShootout.java.weapon.damager.Explosion;
import com.badlogic.gdx.utils.JsonValue;

public class Kinetic extends Ray {

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
