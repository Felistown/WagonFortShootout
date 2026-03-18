package WagonFortShootout.java.weapon.shooter.projectile.custom;

import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.weapon.shooter.Shootable;
import WagonFortShootout.java.weapon.shooter.ShootableTypes;
import com.badlogic.gdx.utils.JsonValue;

public class RocketAp extends RocketHe {

    private final Shootable kinetic;

    public RocketAp(JsonValue value) {
        super(value);
        kinetic = ShootableTypes.get(value.get("kinetic"));
    }

    @Override
    public void onHit(HitResult data) {
        kinetic.shoot(data.shooter, data.hitPos, data.shooter.getFacing(), 0);
        super.onHit(data);
    }
}
