package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class GunEntity extends Entity {

    protected Gun.Instance gun;

    public GunEntity(Vector2 pos, Sprite sprite, int health, int size, int stopping, String gun) {
        super(pos, sprite, health, size, stopping);
        this.gun = Gun.getGun(gun, this);
        FACE.setSpeed(this.gun.getSpeed());
    }

    @Override
    public void onRemove() {
        super.onRemove();
        gun.remove();
    }

    public Gun.Instance getGun() {
        return gun;
    }
}
