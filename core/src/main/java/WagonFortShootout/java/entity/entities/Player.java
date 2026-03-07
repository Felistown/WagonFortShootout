package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends GunEntity {

    public Player(Vector2 pos) {
        super(pos, new Sprite(new Texture("image/player.png")), Mth.circle( (float) 1/ 2, 8),1000,1,5, "controllable","lever_rifle");
    }

    @Override
    public void onHit(HitData data) {
        super.onHit(data);
        GameLevel.SCREEN_SHAKER.rumble(data.weight * 2.5f, 5);
    }

    @Override
    public void onRemove() {
        GameLevel.player = new Player(new Vector2(95,95));
        super.onRemove();
    }
}
