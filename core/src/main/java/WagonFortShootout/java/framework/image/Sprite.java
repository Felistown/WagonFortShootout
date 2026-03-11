package WagonFortShootout.java.framework.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.PriorityQueue;

public class Sprite implements Comparable<Sprite>{

    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas(Gdx.files.internal("texture_atlas/game_assets.atlas"));
    private static Array<Sprite> ALL_SPRITES = new Array<Sprite>();

    protected int layer;
    protected TextureRegion texture;
    protected final Vector2 size;
    protected final Vector2 pos;
    public float rotation;

    public Sprite(String texture, int layer) {
        this(TEXTURE_ATLAS.findRegion(texture), layer);
    }

    public Sprite(TextureRegion texture, int layer) {
        this.texture = texture;
        this.layer = layer;
        size = new Vector2(1,1);
        pos = new Vector2();
        rotation = 0;
        ALL_SPRITES.add(this);
        ALL_SPRITES.sort();
    }

    public void setSize(float width, float height) {
        size.set(width,height);
    }

    public Vector2 getSize() {
        return size.cpy();
    }

    public void setPos(float x, float y) {
        pos.set(x,y);
    }

    public void setPos(Vector2 pos) {
        this.pos.set(pos);
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        ALL_SPRITES.sort();
    }

    protected boolean draw(SpriteBatch spriteBatch) {
        float width = size.x;
        float height = size.y;
        spriteBatch.draw(texture, pos.x - width / 2, pos.y - height / 2, width / 2, height / 2, width, height,1,1, rotation);
        return false;
    }

    public static void drawAll(SpriteBatch spriteBatch) {
        boolean resort = false;
        for(int i = 0; i < ALL_SPRITES.size; i ++) {
            Sprite current = ALL_SPRITES.get(i);
            if(current.draw(spriteBatch)) {
                resort = true;
                ALL_SPRITES.removeIndex(i);
                i--;
            }
        }
        if(resort) {
            ALL_SPRITES.sort();
        }
    }

    public void remove() {
        ALL_SPRITES.removeValue(this, true);
        ALL_SPRITES.sort();
    }

    public static void dispose() {
        TEXTURE_ATLAS.dispose();
    }

    @Override
    public int compareTo(Sprite o) {
        return Integer.compare(layer, o.layer);
    }
}
