package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.AiEntity;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class GunEntity extends AiEntity {

    public Gun.Instance gun;

    public GunEntity(Vector2 pos, JsonValue value, Team team) {
        super(pos, value, team);
        gun = Gun.getGun(value.getString("gun"), this);
        FACE.setSpeed(this.gun.getSpeed());
    }

    @Override
    public void onRemove() {
        super.onRemove();
        gun.remove();
    }
}
