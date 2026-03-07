package WagonFortShootout.java.framework;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.weapon.Gun;
import WagonFortShootout.java.weapon.damager.Bullet;
import WagonFortShootout.java.weapon.damager.Explosion;
import com.badlogic.gdx.math.Vector2;

public class HitData {

    public final Mutable piercing;
    public final int damage;
    public final float weight;
    public final Vector2 pos;
    public final Entity entity;

    public HitData(int damage, Mutable piercing, float weight, Vector2 pos, Entity entity) {
        this.damage = damage;
        this.piercing= piercing;
        this.weight= weight;
        this.pos = pos;
        this.entity= entity;
    }

    public HitData(Bullet bullet, Mutable piercing, Entity entity, Vector2 pos) {
        damage =bullet.damage;
        weight= bullet.weight;
        this.piercing = piercing;
        this.pos = pos;
        this.entity= entity;
    }
}
