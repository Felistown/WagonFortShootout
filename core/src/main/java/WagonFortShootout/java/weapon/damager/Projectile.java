package WagonFortShootout.java.weapon.damager;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import com.badlogic.gdx.math.Vector2;

public interface Projectile {

    public void shoot(Entity entity, Vector2 pos, float face, float addedSpread);

    //When thing is hit
    public default void onHit(HitResult data) {

    }

    //When entity is killed
    public default void onKill(HitResult data) {

    }

    //When stopped by entity/object
    public default void onStop(HitResult data) {

    }

    //When exiting anchored entity/object
    public default void onExit(HitResult data) {

    }

    //When hitting anchored entity/object
    public default void onEnter(HitResult data) {

    }

    public int getDamage();

    public float getWeight();
}
