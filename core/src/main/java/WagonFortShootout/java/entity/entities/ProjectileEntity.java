package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.annotations.KillOnWorldBounds;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.entity.hitbox.ContactHitbox;
import WagonFortShootout.java.framework.entity.hitbox.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.mutable.MutableNumber;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Consumer;

@KillOnWorldBounds
public class ProjectileEntity extends Entity {

    private final Consumer<HitResult> onFuse;
    private int fuse;

    public ProjectileEntity(Vector2 pos, Sprite sprite, Hitbox.Builder builder, int health, int stopping, float max_speed, float acceleration, Team team, int fuse, float speed, float faceRad, Consumer<HitResult> onfuse, Hitbox ignore) {
        super(pos, sprite, builder, health, stopping, max_speed,acceleration, team);
        this.fuse = fuse;
        FACE.set(faceRad);
        this.onFuse = onfuse;
        ((ContactHitbox)HITBOX).ignore = ignore;
        POS.addVel(faceRad, speed);
    }

    @Override
    public boolean tick() {
        if(fuse > 0) {
            fuse--;
        }
        if(fuse <= 0) {
            onFuse.accept(new HitResult(0, 0, new MutableNumber(0), this, getPos(), null));
        }
        return super.tick();
    }
}
