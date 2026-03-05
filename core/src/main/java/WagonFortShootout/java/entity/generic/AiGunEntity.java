package WagonFortShootout.java.entity.generic;

import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.entity.Hitbox;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public abstract class AiGunEntity extends GunEntity implements AiEntity {

    private final Ai ai;

    public AiGunEntity(Vector2 pos, String sprite, Polygon hitbox, int health, int size, int stopping, String gun, String ai) {
        super(pos, new Sprite(new Texture(sprite)), hitbox,health, size, stopping, gun);
        this.ai = Ai.fromType(ai, this);
    }

    public void onRemove() {
        super.onRemove();
    }

    @Override
    public void stopAi() {
        Gdx.app.log("Ai", "Stopped Ai");
        ai.remove();
    }
}
