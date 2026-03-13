package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public abstract class AiEntity extends Entity {

    private final Ai AI;

    public AiEntity(Vector2 pos, Sprite sprite, Hitbox.Builder hitbox, int health, int size, int stopping, String ai, Team team) {
        super(pos, sprite, hitbox,health, size, stopping, team);
        this.AI = Ai.fromType(ai, this);
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
