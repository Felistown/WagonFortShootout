package WagonFortShootout.java.entity;

import WagonFortShootout.java.utils.Face;
import WagonFortShootout.java.utils.Pos;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class Entity {

    private static final ArrayList<Entity> ALL_ENTITIES = new ArrayList<Entity>();
    protected final Pos POS;
    protected final Face FACE;
    private Sprite sprite;
    private Circle hitbox;
    private boolean remove;

    public Entity(Vector2 pos, Sprite sprite, int size) {
        POS = new Pos(pos);
        FACE =  new Face(Math.PI, 2 * Math.PI);
        this.sprite = sprite;
        sprite.setSize(size,size);
        hitbox = new Circle(pos, (float)size / 2);
        ALL_ENTITIES.add(this);
    }

    public void draw(SpriteBatch spriteBatch) {
        Vector2 pos = POS.pos();
        sprite.setCenter(pos.x, pos.y);
        sprite.draw(spriteBatch);
    }

    public void tick() {
        POS.logic();
        FACE.tick();
        hitbox.setPosition(POS.pos());
    }

    public static void drawAll(SpriteBatch spriteBatch) {
        for(Entity e: ALL_ENTITIES) {
            e.draw(spriteBatch);
        }
    }

    public static void tickAll() {
        for(Entity e: ALL_ENTITIES) {
            e.tick();
        }
        for(int i = 0; i < ALL_ENTITIES.size(); i++) {
            Entity e = ALL_ENTITIES.get(i);
            if(e.toRemove()) {
                ALL_ENTITIES.remove(e);
                i--;
            }
        }
    }

    public Circle getHitbox() {
        return hitbox;
    }

    public void onHit() {
        remove = true;
    }

    public boolean toRemove() {
        return remove;
    }

    public static ArrayList<Entity> getAllEntities() {
        return ALL_ENTITIES;
    }
}
