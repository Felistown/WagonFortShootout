package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.hitbox.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public abstract class AiEntity extends Entity {

    private final Ai AI;

    public AiEntity(Vector2 pos, Sprite sprite, Hitbox.Builder hitbox, int health, int stopping, String ai, float max_speed, float acceleration, Team team) {
        super(pos, sprite, hitbox,health, stopping,max_speed,acceleration, team);
        this.AI = Ai.fromType(ai, this);
    }

    public AiEntity(Vector2 pos, JsonValue value, Team team) {
        super(pos, value, team);
        this.AI = Ai.fromType(value.getString("ai"), this);
    }

    @Override
    public boolean tick() {
        AI.tick();
        return super.tick();
    }

    @Override
    public void onRemove() {
        AI.remove();
        super.onRemove();
    }
}
