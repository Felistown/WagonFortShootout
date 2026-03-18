package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Tank extends Mount {

    private final String main;
    private final String secondary;
    private final String tertiary;

    private Sprite turret;
    private Gun.Instance mg;
    private Gun.Instance mounter_gun;
    private float switchCooldown;
    private boolean isHe;

    public Tank(Vector2 pos, JsonValue value, Team team) {
        super(pos, value, team);
        turret = Sprite.fromJson(value.get("turret"));
        turning_speed = value.getFloat("turning_speed");
        main = value.getString("main");
        secondary = value.getString("secondary");
        tertiary = value.getString("tertiary");
        HITBOX.setTransparent(true);
        HITBOX.setAnchored(true);
        switchCooldown = 0;
        isHe = false;
    }

    @Override
    public void mount(Entity mounter) {
        super.mount(mounter);
        mg = Gun.getGun(tertiary, mounter);
        mounter_gun = ((GunEntity)mounter).gun;
        ((GunEntity)mounter).gun = Gun.getGun(main, mounter);
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
                mounter.setPosAndRot(pos, (float)Math.toDegrees(mounter.getFacing()));
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
                g.gun = Gun.getGun(main, mounter);
            } else {
                isHe = true;
                g.gun = Gun.getGun(secondary, mounter);
            }
        }
        return super.tick();
    }

    @Override
    public void onHit(HitResult data) {
        if(data.shooter != mounter) {
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
