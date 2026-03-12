package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public abstract class Mount extends Entity {

    protected Entity mounter;
    protected float turning_speed;

    public Mount(Vector2 pos, Sprite sprite, Hitbox.Builder polygon, int health, int size, int stopping) {
        super(pos, sprite,polygon, health, size, stopping);
        FACE.setSpeed(Math.PI);
        turning_speed = 0.025f;
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
    public boolean tick() {
        if(mounter != null) {
            mounter.POS.setPos(getPos());
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
