package WagonFortShootout.java.weapon.shooter.projectile;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.ProjectileEntity;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.entity.hitbox.ContactHitbox;
import WagonFortShootout.java.framework.entity.hitbox.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.weapon.shooter.Shootable;
import WagonFortShootout.java.weapon.shooter.ShootableTypes;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.jar.JarEntry;

public class Projectile implements Shootable {

    private final JsonValue sprite;
    private final Hitbox.Builder builder;
    private final int stopping;
    private final int health;
    private final float drag;
    public final float SPREAD;
    private final int fuse;
    private final float speed;


    public Projectile(JsonValue value) {
        sprite = value.get("sprite");
        builder =  ContactHitbox.ContactHitboxBuilder.fromJson(value.get("hitbox"), this::onHit);
        health = value.getInt("health");
        drag = value.getFloat("drag");
        stopping = value.getInt("stopping");
        fuse = value.getInt("fuse");
        speed = value.getFloat("speed");
        SPREAD = value.getFloat("spread");
    }

    @Override
    public void shoot(Entity entity, Vector2 pos, float face, float addedSpread) {
        addedSpread += SPREAD;
        new ProjectileEntity(pos, Sprite.fromJson(sprite), builder, health, stopping, Float.MAX_VALUE, drag, Team.neutral(), fuse, speed, face + (float)(Math.random() * addedSpread - addedSpread / 2), this::onStop, entity.HITBOX);
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getWeight() {
        return 0;
    }
}
