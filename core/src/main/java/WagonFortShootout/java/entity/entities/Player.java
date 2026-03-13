package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player extends GunEntity {

    public Player(Vector2 pos, Team team) {
        super(pos, new Sprite("player", 1), Hitbox.Builder.circle((float) 1/ 2, 8),300,1,5, "controllable","revolver", team);
    }

    @Override
    public void onHit(HitData data) {
        super.onHit(data);
        GameLevel.SCREEN_SHAKER.rumble(data.weight * 2.5f, 5);
    }

    @Override
    public void onRemove() {
        GameLevel.player = new Player(new Vector2(95,95), Team.unaffiliated());
        super.onRemove();
    }
}
