package WagonFortShootout.java.rays;

import WagonFortShootout.java.entity.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

public class Bullet {

    private static final Texture TEXTURE = new Texture("bullet.png");
    private static final int DIST = 1;

    private int damage;
    private Entity shooter;

    public Bullet(int damage, Entity shooter) {
        this.damage = damage;
        this.shooter = shooter;
    }

    public void shoot(Vector2 pos, double angle) {
            TrailEffect trail = new TrailEffect(pos, angle, TEXTURE);
            trail.display(5, DIST);
            Ray detection = new Ray(new Vector3(pos, 0), new Vector3(-(float) Math.cos(angle), -(float) Math.sin(angle), 0).scl(DIST));
            ArrayList<Entity> hitEntities = new ArrayList<Entity>();
            for (Entity e : Entity.getAllEntities()) {
                Circle hitbox = e.getHitbox();
                if (e != shooter && Intersector.intersectRaySphere(detection, new Vector3(hitbox.x, hitbox.y, 0), hitbox.radius, null)) {
                    hitEntities.add(e);
                }
            }
            hitEntities.forEach(Entity::onHit);
    }
}
