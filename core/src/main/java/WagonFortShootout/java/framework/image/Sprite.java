package WagonFortShootout.java.framework.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Sprite implements Comparable<Sprite>{

    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas(Gdx.files.internal("texture_atlas/game_assets.atlas"));
    private static final Array<Sprite> ALL_SPRITES = new Array<Sprite>();
    private static final Array<Sprite> TO_ADD = new Array<Sprite>();
    private static final Array<Sprite> TO_REMOVE = new Array<Sprite>();

    protected int layer;
    protected TextureAtlas.AtlasSprite sprite;

    public Sprite(String texture, int layer) {
        this(TEXTURE_ATLAS.findRegion(texture), layer);
    }

    public Sprite(TextureAtlas.AtlasRegion texture, int layer) {
        this.sprite = new TextureAtlas.AtlasSprite(texture);
        this.layer = layer;
        TO_ADD.add(this);
    }

    public void setSize(float width, float height) {
        float x = sprite.getX() + sprite.getWidth() / 2;
        float y = sprite.getY() + sprite.getHeight() / 2;
        sprite.setSize(width,height);
        sprite.setCenter(x,y);
        sprite.setOriginCenter();
    }

    public void setPos(float x, float y) {
        sprite.setCenter(x,y);
        sprite.setOriginCenter();
    }

    public void setPos(Vector2 pos) {
        setPos(pos.x, pos.y);
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        ALL_SPRITES.sort();
    }

    protected boolean draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
        return false;
    }

    public void setRotationRad(float rotation) {
        sprite.setRotation((float)Math.toDegrees(rotation));
    }

    public static void drawAll(SpriteBatch spriteBatch) {
        if(TO_REMOVE.size > 0 || TO_ADD.size > 0) {
            ALL_SPRITES.removeAll(TO_REMOVE, true);
            TO_REMOVE.clear();
            ALL_SPRITES.addAll(TO_ADD);
            TO_ADD.clear();
            ALL_SPRITES.sort();
        }
        for(Sprite s: ALL_SPRITES) {
            if(s.draw(spriteBatch)) {
                TO_REMOVE.add(s);
            }
        }
    }

    public void remove() {
        TO_REMOVE.add(this);
    }

    public static void dispose() {
        TEXTURE_ATLAS.dispose();
    }

    @Override
    public int compareTo(Sprite o) {
        return Integer.compare(layer, o.layer);
    }
}
