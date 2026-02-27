package WagonFortShootout.java;

import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;

public class Bullet {

    private static final int DIST = 100;

    private int damage;
    private Entity shooter;

    public Bullet(int damage, Entity shooter) {
        this.damage = damage;
        this.shooter = shooter;
    }

    public void shoot(Vector2 pos, double angle) {
            Ray detection = new Ray(new Vector3(pos, 0), new Vector3(-(float) Math.cos(angle), -(float) Math.sin(angle), 0).scl(DIST));
            Entity hitEntity = null;
            Vector2 temp = Mth.toVec(angle, DIST).add(pos);
            Vector3 hitPos = new Vector3(temp.x, temp.y, 0);
            for (Entity e : Entity.getAllEntities()) {
                Circle hitbox = e.getHitbox();
                if (e != shooter && Intersector.intersectRaySphere(detection, new Vector3(hitbox.x, hitbox.y, 0), hitbox.radius, hitPos)) {
                    hitEntity = e;
                    break;
                }
            }
            Beam.beam(pos, new Vector2(hitPos.x, hitPos.y),0.25f, 5, new Color(255, 214, 0,1));
            if(hitEntity != null) {
                hitEntity.onHit();
            }
    }
}
