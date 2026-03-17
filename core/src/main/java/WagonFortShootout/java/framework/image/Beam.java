package WagonFortShootout.java.framework.image;

import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Beam {

    public static final Beam DEBUG_BEAM = new Beam("missing_texture", 10, 100, 0.25f);

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

    public static Beam fromJson(JsonValue value) {
        return new Beam(value.getString("texture"), value.getInt("layer"),value.getInt("lifetime"), value.getFloat("width"));
    }

    public Instance instance(Vector2 from, Vector2 to) {
        Instance instance = new Instance();
        instance.point(from, to);
        return instance;
    }

    public class Instance extends Sprite {

        private int lifeTime;

        public Instance() {
            super(texture, Beam.this.layer,0,0);
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
            setCentre(Mth.mid(from, to));
            setSize(from.dst(to), width);
            setRotationRad(Mth.angleRad(from, to));
        }

        public void setLength(float length) {
            setSize(length, width);
        }
    }
}
