package WagonFortShootout.java.framework.data;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.entity.HitboxHolder;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.weapon.damager.Beam;
import WagonFortShootout.java.weapon.damager.Projectile;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Consumer;

public class HitResult {

    public static final Consumer<HitResult> NO_OP = x -> {};

    public final Mutable piercing;
    public final int damage;
    public final float weight;
    public final Entity shooter;
    public final Vector2 hitPos;
    public final HitboxHolder holder;
    public final float direction;

    public HitResult(int damage, float weight, Mutable piercing, Entity shooter, Vector2 hitPos, HitboxHolder holder) {
        this.damage = damage;
        this.piercing= piercing;
        this.weight= weight;
        this.shooter = shooter;
        this.hitPos = hitPos;
        this.holder = holder;
        direction = Mth.angleRad(shooter.getPos(), hitPos);
    }

    public HitResult(Projectile bullet, Mutable piercing, Entity shooter, Vector2 hitPos, HitboxHolder holder) {
        damage =bullet.getDamage();
        weight= bullet.getWeight();
        this.piercing = piercing;
        this.shooter = shooter;
        this.hitPos = hitPos;
        this.holder = holder;
        direction = Mth.angleRad(shooter.getPos(), hitPos);
    }
}
