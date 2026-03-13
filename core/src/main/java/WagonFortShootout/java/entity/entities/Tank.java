package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Tank extends Mount {

    private Sprite turret;
    private Gun.Instance mg;
    private Gun.Instance mounter_gun;
    private float switchCooldown;
    private boolean isHe;

    public Tank(Vector2 pos, Team team) {
        super(pos,new Sprite("tank", 0),
            Hitbox.Builder.empty()
                .addSub(Mth.rectange(5, 1), new Vector2(-0.5f, 1))
                .addSub(Mth.rectange(1, 2), new Vector2(2.5f, 0.5f))
                .addSub(Mth.rectange(5, 1), new Vector2(0.5f,-1))
                .addSub(Mth.rectange(1, 2), new Vector2(-2.5f, -0.5f)),
            10000, 9, 1000, team);
        getSprite().setSize(6,3);
        turret = new Sprite("tank_turret", 3);
        turret.setSize(2.5f, 8.92857142857f);
        turning_speed = 0.005f;
        POS.max_speed = 0.1f;
        POS.acceleration = 0.001f;
        HITBOX.setTransparent(true);
        HITBOX.setAnchored(true);
        switchCooldown = 0;
        isHe = false;
    }

    @Override
    public void mount(Entity mounter) {
        super.mount(mounter);
        mg = Gun.getGun("tank_mg", mounter);
        mounter_gun = ((GunEntity)mounter).gun;
        ((GunEntity)mounter).gun = Gun.getGun("tank_ap", mounter);
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
        for(float i = 0; i < Math.PI * 2; i += 0.1f) {
            Vector2 pos = getPos().add(Mth.toVec(i, 3));
            if(mounter.HITBOX.traverable(pos)) {
                mounter.POS.setPos(pos);
            }
        }
        super.dismount();
    }

    @Override
    public boolean tick() {
        turret.setPos(getPos());
        switchCooldown = Math.max(0, switchCooldown - 1);
        boolean space = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if(mg != null && space) {
            mg.shoot();
        }
        if(mounter != null) {
            turret.setRotationRad(mounter.getFacing() + (float)Math.PI / 2);
        }
        boolean x = Gdx.input.isKeyPressed(Input.Keys.X);
        if(switchCooldown <= 0 && x && mounter instanceof GunEntity g && g.gun.bullets() >= 1){
            switchCooldown = 200;
            if(isHe) {
                isHe = false;
                g.gun = Gun.getGun("tank_ap", mounter);
            } else {
                isHe = true;
                g.gun = Gun.getGun("tank_he", mounter);
            }
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

    @Override
    public void onRemove() {
        turret.remove();
        super.onRemove();
    }

    @Override
    public void move(Vector2 vec) {
        if(vec.x != 0) {
            mounter.FACE.set(mounter.getFacing() + vec.x * -turning_speed);
        }
        super.move(vec);
    }
}
