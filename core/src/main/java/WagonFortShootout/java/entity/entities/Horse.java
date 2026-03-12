package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Vector2;

public class Horse extends Mount {
    public Horse(Vector2 pos) {
        super(pos, new Sprite("horse", 0), Hitbox.Builder.rectangle(3.85f, 2), 200, 4, 5);
        HITBOX.setTransparent(true);
        POS.max_speed = 0.1f;
        POS.acceleration = 0.003f;
    }

    @Override
    public void onHit(HitData data) {
        if(getMounter() == null) {
            super.onHit(data);
        }
    }
}
