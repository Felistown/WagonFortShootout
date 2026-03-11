package WagonFortShootout.java.entity;

import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Face;
import WagonFortShootout.java.framework.entity.Pos;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Sprite;
import com.badlogic.gdx.Gdx;
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
    protected int stopping;

    public Entity(Vector2 pos, Sprite sprite, Polygon polygon,int health, int size, int stopping) {
        //TODO Make it so that you can instantiate an entity from json
        //TODO change create sprite object to control sprite render states and other things
        POS = new Pos(pos, this);
        FACE = new Face(-1);
        this.sprite = sprite;
        sprite.setSize(size,size);
        HITBOX = new Hitbox(polygon, this::onHit);
        ALL_ENTITIES.add(this);
        MAX_HEALTH = health;
        this.stopping = stopping;
        this.health = MAX_HEALTH;
    }

    public boolean tick() {
        if(health <= 0) {
            return true;
        }
        POS.logic();
        FACE.tick();
        sprite.setRotationRad(getFacing());
        sprite.setPos(getPos());
        HITBOX.setPosition(getPos());
        HITBOX.setRotation((float)Math.toDegrees(getFacing()));
        return false;
    }

    public static void tickAll() {
        Entity[] temp = ALL_ENTITIES.toArray(Entity[]::new);
        for(int i = 0; i < temp.length; i++) {
            Entity e = temp[i];
            if(e.tick()) {
                e.onRemove();
                ALL_ENTITIES.remove(e);
            }
        }
    }

    public void onRemove() {
        Gdx.app.log("Entity", "Entity removed");
        if(mount != null) {
            mount.dismount();
        }
        HITBOX.remove();
        sprite.remove();
    }

    public void onHit(HitData data) {
        health -= data.damage;
        data.piercing.sub(stopping);
        POS.addVel(data.pos.sub(POS.pos()).angleRad(), data.weight * 0.05f);
        FACE.recoil(data.weight, data.weight / 2);
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
