package WagonFortShootout.java.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;

public class Effect {

    public static final int max_layers = 5;
    private static final ArrayList<ArrayList<Effect>> ALL_EFFECTS = new ArrayList<ArrayList<Effect>>(max_layers);

    private final Sprite SPRITE;
    private int lifetime;

    public static void init() {
        for(int i = 0; i < max_layers; i++) {
            ALL_EFFECTS.add(i, new ArrayList<Effect>());
        }
    }

    public static void addEffect(Effect effect, int lifeTime, Vector2 pos, float angle, int layer) {
        effect.lifetime = lifeTime;
        effect.SPRITE.setCenter(pos.x, pos.y);
        effect.SPRITE.setOriginCenter();
        effect.SPRITE.setRotation(angle);
        if(layer <  max_layers) {
            ALL_EFFECTS.get(layer).add(effect);
        }
    }

    public Effect(Texture texture, float length, float height) {
        SPRITE = new Sprite(texture);
        SPRITE.setSize(length, height);
        SPRITE.setCenter(0,0);
    }

    public static void renderLayer(int from, int to, SpriteBatch spriteBatch) {
        for(int i = from; i <= to; i ++) {
            ArrayList<Effect> set = ALL_EFFECTS.get(i);
            for (int j = 0; j < set.size(); j++) {
                Effect r = set.get(j);
                r.render(spriteBatch);
                if (r.lifetime <= 0) {
                    set.remove(r);
                    j--;
                }
            }
        }
    }

    public void render(SpriteBatch spriteBatch) {
        SPRITE.draw(spriteBatch);
        lifetime --;
    }
}
