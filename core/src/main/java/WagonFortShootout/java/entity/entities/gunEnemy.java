package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.AiGunEntity;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.math.Vector2;

public class gunEnemy extends AiGunEntity {

    public gunEnemy(Vector2 pos) {
        super(pos, "image/circle.png", Mth.circle( (float) 1/ 2, 8),100,1, 5,"lever_rifle", "gun_enemy");
    }

}
