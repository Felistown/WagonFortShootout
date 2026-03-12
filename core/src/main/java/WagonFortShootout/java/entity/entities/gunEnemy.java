package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.framework.image.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class gunEnemy extends GunEntity {

    public gunEnemy(Vector2 pos) {
        super(pos, new Sprite("circle", 1), Hitbox.Builder.circle((float) 1/ 2, 8),100,1, 5,"gun_enemy","lever_rifle");
        POS.max_speed = 0.05f;
    }

}
