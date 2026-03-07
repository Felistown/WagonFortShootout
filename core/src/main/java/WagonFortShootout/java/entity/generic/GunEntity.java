package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public abstract class GunEntity extends AiEntity {

    public Gun.Instance gun;

    public GunEntity(Vector2 pos, Sprite sprite, Polygon hitbox, int health, int size, int stopping, String ai,String gun) {
        super(pos, sprite,hitbox, health, size, stopping, ai);
        this.gun = Gun.getGun(gun, this);
        FACE.setSpeed(this.gun.getSpeed());
    }

    @Override
    public void onRemove() {
        super.onRemove();
        gun.remove();
    }
}
