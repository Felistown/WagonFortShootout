package WagonFortShootout.java.weapon;

import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.world.Hitbox;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonString;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Gun {

    private static final HashMap<String, Gun> ALL_GUNS = new HashMap<String, Gun>();
    private static final float DIST = 150;
    private static final float WIDTH = 0.25f;
    private static final int LIFETIME = 5;
    private static final Color COLOUR = new Color(255, 214, 0, 1);

    private final int damage;
    private final int projectiles;
    private final float SPREAD;
    private final int piercing;
    private final int maxBullets;
    private final int fireRate;
    private final int reloadRate;
    private final Sound fire;
    private final Sound empty;
    private final Sound reload;
    private final Effect EFFECT;
    private final Sprite SPRITE;
    private final Vector2 OFFSET;
    private final float knockBack;
    private final float recoilMult;
    private final float minRecoil;
    private final float speed;

    public static void init() {
        JsonReader reader = new JsonReader();
        JsonValue file = reader.parse(Gdx.files.internal("data/guns.json"));
        JsonValue current = file.child();
        do {
            JsonValue sound = current.get("sound");
            JsonValue flash = current.get("effect");
            JsonValue sprite = current.get("gun");
            int damage = current.getInt("damage");
            int maxBullets = current.getInt("maxBullets");
            int projectiles = current.getInt("projectiles");
            float spread = current.getFloat("spread");
            int piercing = current.getInt("piercing");
            int fireRate = current.getInt("fireRate");
            int reloadRate = current.getInt("reloadRate");
            float knockBack = current.getFloat("knockBack");
            float recoilMult = current.getFloat("recoilMult");
            float minRecoil = current.getFloat("minRecoil");
            float speed = current.getFloat("speed");
            String fire = sound.getString("fire");
            String empty = sound.getString("empty");
            String reload = sound.getString("reload");
            float flash_size = flash.getFloat("size");
            Effect effect = new Effect(new Texture(flash.get("texture").asString()), flash_size, flash_size);
            float sprite_size = sprite.getFloat("size");
            Sprite texture = new Sprite(new Texture(sprite.getString("texture")));
            texture.setScale(sprite_size/texture.getWidth());
            Vector2 offset = new Vector2(sprite.getFloat("xOffset"), sprite.getFloat("yOffset"));
            Gun gun = new Gun(damage, projectiles, spread, piercing, maxBullets, fireRate, reloadRate, knockBack, recoilMult, minRecoil, speed, fire, empty, reload, effect, texture, offset);
            ALL_GUNS.put(current.name(), gun);
            current = current.next;
        } while (current != null);
    }

    public static Gun.Instance getGun(String name, Entity entity) {
        if(ALL_GUNS.containsKey(name)) {
            return ALL_GUNS.get(name).new Instance(entity);
        } else {
            throw new RuntimeException("No such gun \"" + name + "\".");
        }
    }

    private Gun(int damage, int projectiles, float spread, int piercing, int maxBullets, int fireRate, int reloadRate, float knockBack, float recoilMult, float minRecoil, float speed, String fire, String empty, String reload, Effect effect, Sprite sprite, Vector2 offset) {
        this.damage = damage;
        this.projectiles = projectiles;
        SPREAD = spread;
        this.piercing = piercing;
        this.maxBullets = maxBullets;
        this.fireRate = fireRate;
        this.reloadRate = reloadRate;
        this.knockBack = knockBack;
        this.recoilMult = recoilMult;
        this.minRecoil = minRecoil;
        this.speed = speed;
        this.fire = Gdx.audio.newSound(Gdx.files.internal(fire));
        this.empty = Gdx.audio.newSound(Gdx.files.internal(empty));
        this.reload = Gdx.audio.newSound(Gdx.files.internal(reload));
        this.EFFECT = effect;
        SPRITE = sprite;
        OFFSET = offset;
    }

    public class Instance {

        private static HashSet<Instance> ALL_INSTANCES = new HashSet<Instance>();

        private int bullets;
        private int fireTimer;
        private int reloadTimer;
        private final Entity ENTITY;

        private Instance(Entity entity) {
            bullets = maxBullets;
            fireTimer = 0;
            reloadTimer = 0;
            ALL_INSTANCES.add(this);
            ENTITY = entity;
        }

        public void remove() {
            ALL_INSTANCES.remove(this);
        }

        public static void tickAll() {
            ALL_INSTANCES.forEach(Instance::tick);
        }

        public void tick() {
            fireTimer = Math.max(0, fireTimer - 1);
            reloadTimer = Math.max(0, reloadTimer - 1);
            if(reloadTimer == 1) {
                if(bullets <= 0) {
                    fireTimer += fireRate;
                }
                bullets++;
                reloadTimer = 0;
            }
        }

        public static void renderAll(SpriteBatch spriteBatch) {
            ALL_INSTANCES.forEach(e -> e.render(spriteBatch));
        }

        public void render(SpriteBatch spriteBatch) {
            Vector2 pos = ENTITY.getPOS().pos();
            float facing = (float)ENTITY.getFACE().getFacing();
            SPRITE.setCenter(pos.x, pos.y);
            SPRITE.setOriginCenter();
            SPRITE.setRotation((float)Math.toDegrees(facing - Math.PI));
            Vector2 added = OFFSET.cpy().rotateRad(facing);
            SPRITE.setCenter(pos.x + added.x, pos.y + added.y);
            SPRITE.draw(spriteBatch);
        }

        public void shoot() {
            if(fireTimer <= 0 && reloadTimer <= 0) {
                fireTimer += fireRate;
                if(bullets <= 0) {
                    empty.play();
                } else {
                    Effect.addEffect(EFFECT, 5, ENTITY.getPOS().pos(), (float) Math.toDegrees(ENTITY.getFACE().getFacing() + Math.PI));
                    bullets--;
                    for(int i = 0; i < projectiles; i ++) {
                        fire(piercing, SPREAD);
                    }
                    fire.play();
                    ENTITY.getPOS().addVel((float) (ENTITY.getFACE().getFacing() + Math.PI), knockBack);
                    ENTITY.getFACE().recoil(recoilMult, minRecoil);
                }
            }
        }

        private void fire(int piercing, float maxSpread) {
            Vector2 pos = ENTITY.getPOS().pos();
            float face = (float) ENTITY.getFACE().getFacing();
            Vector2 direction = Mth.toVec(face, DIST).add(pos).add(Mth.randomVec(-maxSpread / 2, maxSpread));
            HashSet<Entity> hitEntities = new HashSet<Entity>();
            Entity[] all = Utils.byClosest(ENTITY);
            for (int i = 0; i < all.length; i ++) {
                Entity e = all[i];
                Hitbox hitbox = e.getHitbox();
                Vector2 eHit = new Vector2();
                if (e != ENTITY && hitbox.rayIntersection(pos, direction, eHit)) {
                    hitEntities.add(e);
                    piercing -= hitbox.RESISTANCE;
                    if(piercing <= 0) {
                        direction = eHit;
                        break;
                    }
                }
            }
            Beam.beam(pos, direction, WIDTH, LIFETIME, COLOUR);
            hitEntities.forEach(e -> e.onHit(damage));
        }

        public void reload() {
            if(reloadTimer <= 0 && bullets < maxBullets && (fireTimer <= 0 || bullets <= 0)) {
                reloadTimer += reloadRate;
                reload.play();
            }
        }

        public int bullets() {
            return bullets;
        }

        public int maxBullets() {
            return maxBullets;
        }

        public int reloadTimer() {
            return reloadTimer;
        }

        public int fireTimer() {
            return fireTimer;
        }

        public int reloadRate() {
            return reloadRate;
        }

        public int fireRate() {
            return fireRate;
        }

        public float getSpeed() {
            return speed;
        }
    }
}
