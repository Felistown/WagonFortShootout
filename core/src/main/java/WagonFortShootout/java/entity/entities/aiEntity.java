package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.ai.Ai;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class aiEntity extends Entity {

    private final Ai ai;

    public aiEntity(Vector2 pos, String sprite,int health, int size, int stopping, String gun, String ai) {
        super(pos, new Sprite(new Texture(sprite)), health, size, stopping, gun);
        this.ai = Ai.fromType(ai, this);
    }

    public void onRemove() {
        super.onRemove();
        ai.remove();
    }
}
