package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public abstract class Mount extends Entity {

    protected Entity mounter;
    protected float turning_speed;
    protected Vector2 mounter_offset;

    public Mount(Vector2 pos, Sprite sprite, Hitbox.Builder polygon, int health, int stopping, float max_speed, float acceleration, Team team) {
        super(pos, sprite,polygon, health, stopping,max_speed, acceleration, team);
        FACE.setSpeed(Math.PI);
        turning_speed = 0.025f;
        mounter_offset = new Vector2(0,0);
    }

    public Mount(Vector2 pos, JsonValue value, Team team) {
        super(pos, value, team);
        FACE.setSpeed(Math.PI);
        turning_speed = 0.025f;
        mounter_offset = new Vector2(0,0);
    }

    public void mount(Entity mounter) {
        this.mounter = mounter;
        mounter.mount = this;
    }

    public void dismount() {
        mounter.mount = null;
        this.mounter = null;
    }

    @Override
    public void onRemove() {
        if(mounter != null) {
            dismount();
        }
        super.onRemove();
    }

    @Override
    public void onHit(HitResult data) {
        super.onHit(data);
    }

    @Override
    public boolean tick() {
        if(mounter != null) {
            if(mounter_offset.len() <= 0) {
                mounter.setPosAndRot(getPos(), (float)Math.toDegrees(mounter.getFacing()));
            } else {
                mounter.setPosAndRot(getPos().add(mounter_offset.cpy().setAngleRad(getFacing())), (float)Math.toDegrees(mounter.getFacing()));
            }
        }
        return super.tick();
    }

    @Override
    public void move(Vector2 vec) {
        FACE.setGoal(getFacing() + vec.x * -turning_speed);
        super.move(Mth.toVec(getFacing(), 1).scl(vec.y));
    }

    @Override
    public void setGoal(double goal) {
        mounter.FACE.setGoal(goal);
    }

    public Entity getMounter() {
        return mounter;
    }
}
