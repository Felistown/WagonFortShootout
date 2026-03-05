package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class Mount extends Entity {

    private Entity mounter;

    public Mount(Vector2 pos, Sprite sprite, int health, int size, int stopping) {
        super(pos, sprite, health, size, stopping);
        FACE.setSpeed(Math.PI);
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
        if(mount != null) {
            dismount();
        }
        super.onRemove();
    }

    @Override
    public void onHit(HitData data) {
        if(mounter == null) {
            super.onHit(data);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(mounter != null) {
            mounter.POS.setPos(getPos());
        }
    }

    @Override
    public void move(Vector2 vec) {
        FACE.setGoal(getFacing() + vec.x * -0.025f);
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
