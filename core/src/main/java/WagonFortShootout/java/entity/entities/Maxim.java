package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.MutableConsumer;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.math.Vector2;

public class Maxim extends Mount {

    public static float max_traverse = 0.174533f;

    private Gun.Instance mounter_gun;

    public Maxim(Vector2 pos, Team team) {
        super(pos, new Sprite("maxim", 0, 2.5f, 1.375f),
            Hitbox.Builder.rectangle(2.5f, 1.375f)
                .addSub(Mth.rectange(0.2f, 1.375f), new Vector2(0, 0), MutableConsumer.putKey(1))
                .addSub(Mth.rectange(1.25f, 1.125f), new Vector2(1, 0), MutableConsumer.putKey(2)),
            10000, 0,0.005f, 0.001f, team);
        MutableConsumer.set(1, this::hitGun);
        MutableConsumer.set(2, this::hitGunBack);
        HITBOX.setTransparent(true);
        stopping = 100;
        turning_speed =0.00072722083f;
        mounter_offset.set(1, 0);
    }

    @Override
    public void mount(Entity mounter) {
        mounter_gun = ((GunEntity)mounter).gun;
        mounter_gun.setVisible(false);
        ((GunEntity)mounter).gun = Gun.getGun("maxim", mounter);
        mounter.FACE.setSpeed(((GunEntity) mounter).gun.getSpeed());
        super.mount(mounter);
    }

    @Override
    public void dismount() {
        ((GunEntity)mounter).gun.remove();
        ((GunEntity)mounter).gun = mounter_gun;
        mounter_gun.setVisible(true);
        mounter.FACE.setSpeed(((GunEntity) mounter).gun.getSpeed());
        mounter_gun = null;
        super.dismount();
    }

    @Override
    public boolean tick() {
        if(mounter != null) {
            mounter.FACE.set((float) Math.max(mounter.getFacing(), getFacing() - max_traverse));
            mounter.FACE.set((float) Math.min(mounter.getFacing(), getFacing() + max_traverse));
        }
        return super.tick();
    }

    @Override
    public void onHit(HitResult data) {
    }


    public void hitGun(HitResult data) {
        if (data.shooter != mounter) {
            health -= data.damage;
            data.piercing.sub(stopping);
        }
    }

    public void hitGunBack(HitResult data) {
        if (data.shooter != mounter) {
            health -= data.damage * 50;
        }
    }
}
