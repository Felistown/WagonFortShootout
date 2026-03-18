package WagonFortShootout.java.framework.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class Sprite implements Comparable<Sprite>{

    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas(Gdx.files.internal("texture_atlas/game_assets.atlas"));
    private static final Array<Sprite> ALL_SPRITES = new Array<Sprite>();
    private static final Array<Sprite> TO_ADD = new Array<Sprite>();
    private static final Array<Sprite> TO_REMOVE = new Array<Sprite>();

    protected int layer;
    protected TextureAtlas.AtlasSprite sprite;

    public Sprite(String texture, int layer, float length, float height) {
        this(TEXTURE_ATLAS.findRegion(texture), layer, length, height);
    }

    public Sprite(TextureAtlas.AtlasRegion texture, int layer, float length, float height) {
        this.sprite = new TextureAtlas.AtlasSprite(texture);
        this.layer = layer;
        setSize(length, height);
        TO_ADD.add(this);
    }

    public void setSize(float width, float height) {
        float x = sprite.getX() + sprite.getWidth() / 2;
        float y = sprite.getY() + sprite.getHeight() / 2;
        sprite.setSize(width,height);
        sprite.setCenter(x,y);
        sprite.setOriginCenter();
    }

    public void setCentre(float x, float y) {
        setPos(x, y);
        sprite.setOriginCenter();
    }

    public void setAlpha(float alpha) {
        sprite.setAlpha(alpha);
    }

    public void setPos(float x, float y) {
        sprite.setCenter(x, y);
    }

    public void setPos(Vector2 pos) {
        setPos(pos.x, pos.y);
    }

    public void setRotCentre(float x, float y) {
        sprite.setOrigin(x, y);
    }

    public void setRotCentre(Vector2 pos) {
        setRotCentre(pos.x, pos.y);
    }

    public void setRotCentre() {
        sprite.setOriginCenter();
    }

    public void setCentre(Vector2 pos) {
        setCentre(pos.x, pos.y);
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

    public void setRotationDeg(float rotation) {
        sprite.setRotation(rotation);
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

    public Sprite copy() {
       return new Sprite(sprite.getAtlasRegion(), layer, sprite.getWidth(), sprite. getHeight());
    }

    public static void dispose() {
        TEXTURE_ATLAS.dispose();
    }

    public static TextureAtlas.AtlasRegion findRegion(String texture) {
        return TEXTURE_ATLAS.findRegion(texture);
    }

    @Override
    public int compareTo(Sprite o) {
        return Integer.compare(layer, o.layer);
    }

    public static Sprite fromJson(JsonValue value) {
        return new Sprite(value.getString("texture"), value.getInt("layer"),value.getFloat("length"), value.getFloat("height"));
    }
}
