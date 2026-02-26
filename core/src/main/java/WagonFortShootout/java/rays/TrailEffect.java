package WagonFortShootout.java.rays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

public class TrailEffect {

    private static final ArrayList<TrailEffect> ALL_RAYS = new ArrayList<TrailEffect>();

    private Texture texture;
    private final Vector2 vector;
    private final Vector2 pos;
    private int lifetime = 1;
    private float dist = 1f;
    private static final float SCALE = 0.25f;

    public TrailEffect(Vector2 pos, double facing, Texture texture) {
        this.vector = new Vector2(-(float)Math.cos(facing), -(float)Math.sin(facing));
        vector.scl(1 + (float)1/texture.getHeight());
        this.pos = pos.add(vector.cpy().scl((texture.getHeight() * SCALE) / 2));
        this.texture = texture;
    }

    public void display(int lifetime, float dist) {
        this.lifetime = lifetime;
        this.dist = dist;
        ALL_RAYS.add(this);
    }

    public static void renderAll(SpriteBatch spriteBatch) {
        for(int i = 0; i < ALL_RAYS.size(); i ++) {
            TrailEffect r = ALL_RAYS.get(i);
            r.render(spriteBatch);
            if(r.lifetime <= 0) {
                ALL_RAYS.remove(r);
                i--;
            }
        }
    }

    public void render(SpriteBatch spriteBatch) {
        float angle = vector.angle() - 90;
        Vector2 temp = new Vector2(pos);
        for(int i = 0; i < dist; i ++) {
            Sprite sprite = new Sprite(texture);
            sprite.setScale(SCALE);
            sprite.rotate(angle);
            sprite.setCenter(temp.x, temp.y);
            sprite.draw(spriteBatch);
            temp.add(vector);
        }
        lifetime --;
    }

    public int getLifetime() {
        return lifetime;
    }
}
