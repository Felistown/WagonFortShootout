package WagonFortShootout.java.framework.image;

import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Beam {

    private TextureAtlas.AtlasRegion texture;
    private final int layer;
    private final int lifetime;
    private final float width;

    public Beam(String texture, int layer, int lifetime, float width) {
        this.texture = Sprite.TEXTURE_ATLAS.findRegion(texture);
        this.layer = layer;
        this.lifetime = lifetime;
        this.width = width;
    }

    public Instance instance(Vector2 from, Vector2 to) {
        Instance instance = new Instance(texture, layer, lifetime);
        instance.point(from, to);
        return instance;
    }

    public class Instance extends Sprite {

        private int lifeTime;

        public Instance(TextureAtlas.AtlasRegion texture, int layer, int lifetime) {
            super(texture, layer);
            this.lifeTime = lifetime;
        }

        @Override
        protected boolean draw(SpriteBatch spriteBatch) {
            super.draw(spriteBatch);
            if(lifeTime > 0) {
                lifeTime--;
            }
            return lifeTime == 0;
        }

        public void point(Vector2 from, Vector2 to) {
            setPos(Mth.mid(from, to));
            setSize(from.dst(to), width);
            setRotationRad(Mth.angleRad(from, to));
        }

        public void setLength(float length) {
            setSize(length, width);
        }
    }
}
