package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.weapon.damager.Ray;
import WagonFortShootout.java.weapon.damager.Projectile;
import com.badlogic.gdx.utils.JsonValue;

import java.util.function.Function;

public class ProjectileTypes {

    public static Projectile get(JsonValue value) {
        String type = value.getString("type");
        for(Type t: Type.values()) {
            if(t.name().toLowerCase().equals(type)) {
                return t.factory.apply(value);
            }
        }
        throw new RuntimeException("Could not find projectile type \"" + type + "\".");
    }

    public enum Type {
        BULLET(Ray::new),
        KINETIC(Kinetic::new),
        EXPLODING(ExplodingBullet::new),
        HYPERLASER(BlueHyperlaser::new);

        private final Function<JsonValue, Projectile> factory;

        private Type(Function<JsonValue, Projectile> factory) {
            this.factory = factory;
        }
    }

}
