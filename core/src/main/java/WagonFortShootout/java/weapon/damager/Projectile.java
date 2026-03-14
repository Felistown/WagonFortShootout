package WagonFortShootout.java.weapon.damager;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import com.badlogic.gdx.math.Vector2;

public interface Projectile {

    public void shoot(Entity entity, Vector2 pos, float face, float addedSpread) ;

    //When entity is hit
    public void onHit(Entity entity);

    //When entity is killed
    public void onKill(Entity entity) ;

    public void onStop(Vector2 pos);

    //When exiting anchored object
    public void onExit(Vector2 pos);

    //When hitting anchored object
    public void onEnter(Vector2 pos);

}
