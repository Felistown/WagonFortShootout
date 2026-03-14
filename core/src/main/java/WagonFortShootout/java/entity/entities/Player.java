package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Player extends GunEntity {

    @Override
    public void onHit(HitData data) {
        super.onHit(data);
        GameLevel.SCREEN_SHAKER.rumble(data.weight * 2.5f, 5);
    }

    private Player(Vector2 pos, JsonValue value, Team team) {
        super(pos, value, team);
    }

    public static Entity load(Vector2 pos, JsonValue value, Team team) {
        if(value.has("ai")) {
            value.get("ai").set("controllable");
        } else {
            value.addChild("ai", new JsonValue("controllable"));
        }
        return new Player(pos, value, team);
    }
}
