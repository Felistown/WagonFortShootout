package WagonFortShootout.java.framework;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.weapon.damager.Beam;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Consumer;

public class HitData {

    public static final Consumer<HitData> NO_OP = x -> {};

    public final Mutable piercing;
    public final int damage;
    public final float weight;
    public final Entity shooter;
    public final Vector2 hitPos;
    public final Entity hit_entity;
    public final float direction;

    public HitData(int damage, float weight, Mutable piercing, Entity shooter, Vector2 hitPos, Entity hit_entity) {
        this.damage = damage;
        this.piercing= piercing;
        this.weight= weight;
        this.shooter = shooter;
        this.hitPos = hitPos;
        this.hit_entity = hit_entity;
        direction = Mth.angleRad(shooter.getPos(), hitPos);
    }

    public HitData(Beam bullet, Mutable piercing, Entity shooter, Vector2 hitPos, Entity hit_entity) {
        damage =bullet.damage;
        weight= bullet.weight;
        this.piercing = piercing;
        this.shooter = shooter;
        this.hitPos = hitPos;
        this.hit_entity = hit_entity;
        direction = Mth.angleRad(shooter.getPos(), hitPos);
    }
}
