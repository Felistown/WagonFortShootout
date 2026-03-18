package WagonFortShootout.java.framework.image;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation extends Sprite{

    private final Frame[] frames;
    private int frame;
    private byte frame_time;
    //TODO finish this class
    public Animation(Frame[] frames, int layer, float length, float height) {
        super((TextureAtlas.AtlasRegion)frames[0].texture, layer, length, height);
        this.frames = frames;
        frame_time = 0;
        frame = -1;
    }

    public void interrupt() {
        frame = -1;
        frame_time = 0;
    }

    public void animate() {
        frame = 0;
        frame_time = 0;
    }

    @Override
    protected boolean draw(SpriteBatch spriteBatch) {
        if(frame >= 0) {
            if(frame >= frames.length) {
                frame_time = 0;
                frame = -1;
            } else {
                sprite.setTexture(frames[frame].texture.getTexture());
                frame_time++;
                if (frame_time >= frames[frame].length) {
                    frame_time = 0;
                    frame++;
                }
            }
        }
        sprite.draw(spriteBatch);
        return false;
    }

    public record Frame(TextureRegion texture, byte length) {

    }
/*
    public class Transformations {

        public final Vector2 pos;
        public final float rotation;
        public final float alpha;

    }

 */
}
