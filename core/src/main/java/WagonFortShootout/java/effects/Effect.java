package WagonFortShootout.java.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Effect {

    private static final ArrayList<Effect> ALL_EFFECTS = new ArrayList<Effect>();

    private final Sprite SPRITE;
    private int lifetime;

    public static void addEffect(Effect effect, int lifeTime, Vector2 pos, float angle) {
        effect.lifetime = lifeTime;
        effect.SPRITE.setCenter(pos.x, pos.y);
        effect.SPRITE.setOriginCenter();
        effect.SPRITE.setRotation(angle);
        ALL_EFFECTS.add(effect);
    }

    public Effect(Texture texture, float length, float height) {
        SPRITE = new Sprite(texture);
        SPRITE.setSize(length, height);
        SPRITE.setCenter(0,0);
    }

    public static void renderAll(SpriteBatch spriteBatch) {
        for(int i = 0; i < ALL_EFFECTS.size(); i ++) {
            Effect r = ALL_EFFECTS.get(i);
            r.render(spriteBatch);
            if(r.lifetime <= 0) {
                ALL_EFFECTS.remove(r);
                i--;
            }
        }
    }

    public void render(SpriteBatch spriteBatch) {
        SPRITE.draw(spriteBatch);
        lifetime --;
    }
}
