package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Horse extends Mount {

    public Horse(Vector2 pos, JsonValue value, Team team) {
        super(pos, value, team);
        HITBOX.setTransparent(true);
    }

    @Override
    public void onHit(HitData data) {
        if(getMounter() == null) {
            super.onHit(data);
        }
    }
}
