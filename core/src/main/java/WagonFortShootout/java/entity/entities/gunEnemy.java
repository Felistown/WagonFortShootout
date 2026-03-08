package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class gunEnemy extends GunEntity {

    public gunEnemy(Vector2 pos) {
        super(pos, new Sprite(new Texture("image/circle.png")), Mth.circle( (float) 1/ 2, 8),100,1, 5,"gun_enemy","musket");
        POS.max_speed = 0.05f;
    }

}
