package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.weapon.damager.Beam;
import WagonFortShootout.java.weapon.damager.Explosion;
import com.badlogic.gdx.math.Vector2;

public class Kinetic extends Beam {

    public final Explosion exploder;

    public Kinetic(int damage, float weight, float Spread, int piercing, Explosion exploder) {
        super(damage, weight, Spread, piercing);
        this.exploder= exploder;
    }

    @Override
    public void onExit(HitResult data) {
        if(!(data.holder instanceof Entity e && e instanceof Mount m && m.getMounter() == data.shooter)) {
            System.out.println(data.holder.getType());
            System.out.println(data.hitPos);
            exploder.explode(data.shooter, data.hitPos, data.direction);
        }
    }
}
