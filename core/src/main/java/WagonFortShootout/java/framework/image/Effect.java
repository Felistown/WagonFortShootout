package WagonFortShootout.java.framework.image;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.JsonValue;

public class Effect {

    public static final Effect DEBUG_EFFECT = new Effect("missing_texture", 10, 100, 1f,1);

    private TextureAtlas.AtlasRegion texture;
    private final int layer;
    private final int lifetime;
    private final float width;
    private final float height;

    public Effect(String texture, int layer, int lifetime, float width, float height) {
        this.texture = Sprite.TEXTURE_ATLAS.findRegion(texture);
        this.layer = layer;
        this.lifetime = lifetime;
        this.width = width;
        this.height = height;
    }

    public Instance instance() {
        return new Instance();
    }

    public static Effect fromJson(JsonValue value) {
        return new Effect(value.getString("texture"), value.getInt("layer"),value.getInt("lifetime"), value.getFloat("length"), value.getFloat("height"));
    }

    public class Instance extends Sprite {

        private int lifeTime;

        public Instance() {
            super(texture, Effect.this.layer ,width, height);
            this.lifeTime = Effect.this.lifetime;
        }

        @Override
        protected boolean draw(SpriteBatch spriteBatch) {
            super.draw(spriteBatch);
            if(lifeTime > 0) {
                lifeTime--;
            }
            return lifeTime == 0;
        }
    }
}
