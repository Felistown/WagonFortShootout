package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.ai.Ai;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public abstract class AiEntity extends Entity {

    private final Ai AI;

    public AiEntity(Vector2 pos, Sprite sprite, Polygon hitbox, int health, int size, int stopping, String ai) {
        super(pos, sprite, hitbox,health, size, stopping);
        this.AI = Ai.fromType(ai, this);
    }

    @Override
    public void tick() {
        AI.tick();
        super.tick();
    }

    @Override
    public void onRemove() {
        AI.remove();
        super.onRemove();
    }
}
