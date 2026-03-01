package WagonFortShootout.java.entity;

import WagonFortShootout.java.utils.Face;
import WagonFortShootout.java.utils.Pos;
import WagonFortShootout.java.weapon.Gun;
import WagonFortShootout.java.world.Hitbox;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Entity {

    private static final HashSet<Entity> ALL_ENTITIES = new HashSet<Entity>();
    protected final Pos POS;
    protected final Face FACE;
    private Sprite sprite;
    private Hitbox hitbox;
    protected Gun.Instance gun;
    protected int health;
    private boolean remove;

    public Entity(Vector2 pos, Sprite sprite, int size, int stopping, String gun) {
        POS = new Pos(pos, this);
        this.gun = Gun.getGun(gun, this);
        FACE = new Face(-1, this.gun.getSpeed());
        this.sprite = sprite;
        sprite.setSize(size,size);
        hitbox = Hitbox.circle(pos, stopping, (float) size/ 2, 8);
        ALL_ENTITIES.add(this);
        health = 100;
    }

    public void draw(SpriteBatch spriteBatch) {
        Vector2 pos = POS.pos();
        sprite.draw(spriteBatch);
    }

    public void tick() {
        if(health <= 0) {
            remove = true;
        }
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
        Entity[] temp = ALL_ENTITIES.toArray(Entity[]::new);
        for(int i = 0; i < temp.length; i++) {
            Entity e = temp[i];
            if(e.toRemove()) {
                e.gun.remove();
                e.hitbox.remove();
                ALL_ENTITIES.remove(e);
            }
        }
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public void onHit(int damage) {
        health -= damage;
    }

    public boolean toRemove() {
        return remove;
    }

    public static HashSet<Entity> getAllEntities() {
        return (HashSet<Entity>)ALL_ENTITIES.clone();
    }

    public Pos getPOS() {
        return POS;
    }

    public Face getFACE() {
        return FACE;
    }

    public Gun.Instance getGun() {
        return gun;
    }

    public int getHealth() {
        return health;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
