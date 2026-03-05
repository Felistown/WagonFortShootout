package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Horse extends Mount {
    public Horse(Vector2 pos) {
        super(pos, new Sprite(new Texture("image/horse.png")),100, 4, 5);
        POS.max_speed = 0.1f;
        POS.acceleration = 0.003f;
    }
}
