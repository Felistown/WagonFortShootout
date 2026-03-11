package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Tank extends Mount {

    private Gun.Instance mg;
    private Gun.Instance mounter_gun;

    public Tank(Vector2 pos) {
        //TODO tank will not stop projectiles when instantiated before mounter
        super(pos,new Sprite("tank", 0), Mth.rectange(9,9), 10000, 9, 1000);
    }

    @Override
    public void mount(Entity mounter) {
        super.mount(mounter);
        mg = Gun.getGun("tank_mg", mounter);
        mounter_gun = ((GunEntity)mounter).gun;
        ((GunEntity)mounter).gun = Gun.getGun("tank_gun", mounter);
        mounter.FACE.setSpeed(((GunEntity) mounter).gun.getSpeed());
    }

    @Override
    public void dismount() {
        mg.remove();
        mg = null;
        ((GunEntity)mounter).gun.remove();
        ((GunEntity)mounter).gun = mounter_gun;
        mounter.FACE.setSpeed(((GunEntity) mounter).gun.getSpeed());
        mounter_gun = null;
        super.dismount();
    }

    @Override
    public boolean tick() {
        boolean space = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if(mg != null && space) {
            mg.shoot();
        }
        return super.tick();
    }

    @Override
    public void onHit(HitData data) {
        if(data.entity != mounter) {
            health -= data.damage;
            data.piercing.sub(stopping);
        }
    }
}
