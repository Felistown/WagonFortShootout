package WagonFortShootout.java.entity;

import WagonFortShootout.java.entity.generic.AiEntity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Face;
import WagonFortShootout.java.framework.entity.Pos;
import WagonFortShootout.java.framework.entity.Hitbox;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

public abstract class Entity {

    private static final HashSet<Entity> ALL_ENTITIES = new HashSet<Entity>();
    public final Pos POS;
    public final Face FACE;
    private Sprite sprite;
    public Mount mount;
    public Hitbox HITBOX;
    public final int MAX_HEALTH;
    protected int health;
    private boolean remove;

    public Entity(Vector2 pos, Sprite sprite, int health, int size, int stopping) {
        //TODO redo this constructor so that entities can have custom hitboxes with different hit behaviour
        POS = new Pos(pos, this);
        FACE = new Face(-1);
        this.sprite = sprite;
        sprite.setSize(size,size);
        HITBOX = Hitbox.circle(pos,this::onHit,stopping, (float) size/ 2, 8);
        ALL_ENTITIES.add(this);
        MAX_HEALTH = health;
        this.health = MAX_HEALTH;
    }

    public void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    public void tick() {
        if(health <= 0) {
            remove = true;
        }
        POS.logic();
        FACE.tick();
        sprite.setRotation((float)Math.toDegrees(getFacing()));
        HITBOX.setPosition(POS.pos());
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
                e.onRemove();
                ALL_ENTITIES.remove(e);
            }
        }
    }

    public void onRemove() {
        Gdx.app.log("Entity", "Entity removed");
        HITBOX.remove();
        if(this instanceof AiEntity e) {
            e.stopAi();
        }
    }

    public void onHit(HitData data) {
        health -= data.damage;
        POS.addVel(data.pos.sub(POS.pos()).angleRad(), data.knockback / 2);
        FACE.recoil(data.recoil_mult / 2, data.min_recoil / 2);
    }

    public boolean toRemove() {
        return remove;
    }

    public static HashSet<Entity> getAllEntities() {
        return (HashSet<Entity>)ALL_ENTITIES.clone();
    }

    public Vector2 getPos() {
        return POS.pos();
    }

    public float getFacing() {
        return (float)FACE.getFacing();
    }

    public Vector2 getVel() {
        return POS.vel();
    }

    public int getHealth() {
        return health;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setGoal(double goal) {
        if(mount == null) {
            FACE.setGoal(goal);
        } else {
            mount.setGoal(goal);
        }
    }

    public void move(Vector2 vec) {
        if(mount == null) {
            POS.move(vec);
        } else {
            mount.move(vec);
        }
    }
}
