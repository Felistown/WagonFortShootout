package WagonFortShootout.java.framework.image;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Effect {

    private TextureRegion texture;
    private final int layer;
    private final int lifetime;
    private final float width;
    private final float height;

    public Effect(String texture, int layer, int lifetime, float width, float height) {
        System.out.println(texture);
        this.texture = Sprite.TEXTURE_ATLAS.findRegion(texture);
        this.layer = layer;
        this.lifetime = lifetime;
        this.width = width;
        this.height = height;
    }

    public Instance instance() {
        Instance i = new Instance(texture, layer, lifetime);
        i.setSize(width, height);
        return i;
    }

    public class Instance extends Sprite {

        private int lifeTime;

        public Instance(TextureRegion texture, int layer, int lifeTime) {
            super(texture, layer);
            this.lifeTime = lifeTime;
        }

        @Override
        protected boolean draw(SpriteBatch spriteBatch) {
            super.draw(spriteBatch);
            lifeTime--;
            return lifeTime <= 0;
        }
    }
}
