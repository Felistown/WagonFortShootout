package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import com.badlogic.gdx.math.Vector2;

public class gunFriend extends GunEntity {

    public gunFriend(Vector2 pos, Team team) {
        super(pos, new Sprite("canny", 1), Hitbox.Builder.circle((float) 1/ 2, 8),100,1, 5,"gun_enemy","tank_he", team);
        POS.max_speed = 0.05f;
    }

}
