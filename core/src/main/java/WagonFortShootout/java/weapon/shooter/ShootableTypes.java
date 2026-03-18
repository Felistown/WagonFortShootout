package WagonFortShootout.java.weapon.shooter;

import WagonFortShootout.java.weapon.shooter.hitscan.Hitscan;
import WagonFortShootout.java.weapon.shooter.hitscan.custom.BlueHyperlaser;
import WagonFortShootout.java.weapon.shooter.hitscan.custom.ExplodingBullet;
import WagonFortShootout.java.weapon.shooter.hitscan.custom.Kinetic;
import WagonFortShootout.java.weapon.shooter.projectile.Projectile;
import WagonFortShootout.java.weapon.shooter.projectile.custom.Grenade;
import WagonFortShootout.java.weapon.shooter.projectile.custom.RocketAp;
import WagonFortShootout.java.weapon.shooter.projectile.custom.RocketHe;
import com.badlogic.gdx.utils.JsonValue;

import java.util.function.Function;

public class ShootableTypes {

    public static Shootable get(JsonValue value) {
        String type = value.getString("type");
        for(Type t: Type.values()) {
            if(t.name().toLowerCase().equals(type)) {
                return t.factory.apply(value);
            }
        }
        throw new RuntimeException("Could not find projectile type \"" + type + "\".");
    }

    public enum Type {
        BULLET(Hitscan::new),
        PROJECTILE(Projectile::new),
        GRENADE(Grenade::new),
        KINETIC(Kinetic::new),
        ROCKET_AP(RocketAp::new),
        ROCKET_HE(RocketHe::new),
        EXPLODING(ExplodingBullet::new),
        HYPERLASER(BlueHyperlaser::new);

        private final Function<JsonValue, Shootable> factory;

        private Type(Function<JsonValue, Shootable> factory) {
            this.factory = factory;
        }
    }

}
