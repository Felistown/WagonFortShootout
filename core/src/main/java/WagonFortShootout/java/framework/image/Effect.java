package WagonFortShootout.java.framework.image;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Effect {

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
        return new Instance(texture, layer, lifetime);
    }

    public class Instance extends Sprite {

        private int lifeTime;

        public Instance(TextureAtlas.AtlasRegion texture, int layer, int lifeTime) {
            super(texture, layer,width, height);
            this.lifeTime = lifeTime;
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
