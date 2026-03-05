package WagonFortShootout.java.framework;

import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.math.Vector2;

public class HitData {

    public final int damage;
    public final float knockback;
    public final float min_recoil;
    public final float recoil_mult;
    public final float rumble;
    public final Vector2 pos;

    public HitData(Gun.Instance gun) {
        this.damage = gun.GUN.damage;
        this.knockback = gun.GUN.knockBack;
        //TODO add projectile weight and do knockback and recoil based on that
        this.min_recoil = gun.GUN.minRecoil;
        this.recoil_mult = gun.GUN.recoilMult;
        rumble = gun.GUN.rumble;
        pos = gun.ENTITY.getPos();
    }
}
