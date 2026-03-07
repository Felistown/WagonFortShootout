package WagonFortShootout.java.weapon;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.Player;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Mutable;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.weapon.damager.Bullet;
import WagonFortShootout.java.weapon.damager.ExplodingBullet;
import WagonFortShootout.java.weapon.damager.Explosion;
import WagonFortShootout.java.weapon.damager.Kinetic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.HashSet;

public class Gun {

    private static final HashMap<String, Gun> ALL_GUNS = new HashMap<String, Gun>();

    public final Bullet bullet;
    public final int projectiles;
    public final int maxBullets;
    public final int fireRate;
    public final int reloadRate;
    public final Sound fire;
    public final Sound empty;
    public final Sound reload;
    public final Effect EFFECT;
    public final Sprite SPRITE;
    public final Vector2 OFFSET;
    public final float knockBack;
    public final float rumble;
    public final float recoilMult;
    public final float minRecoil;
    public final float speed;

    public static void init() {
        JsonReader reader = new JsonReader();
        JsonValue file = reader.parse(Gdx.files.internal("data/guns.json"));
        JsonValue current = file.child();
        do {
            JsonValue sound = current.get("sound");
            JsonValue flash = current.get("effect");
            JsonValue sprite = current.get("gun");
            JsonValue bullet = current.get("bullet");
            int maxBullets = current.getInt("maxBullets");
            int projectiles = current.getInt("projectiles");
            int fireRate = current.getInt("fireRate");
            int reloadRate = current.getInt("reloadRate");
            float knockBack = current.getFloat("knockBack");
            float rumble = current.getFloat("rumble");
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
            Gun gun = new Gun(Bullet.readJson(bullet), projectiles, maxBullets, fireRate, reloadRate, knockBack, rumble, recoilMult, minRecoil, speed, fire, empty, reload, effect, texture, offset);
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

    private Gun(Bullet bullet, int projectiles, int maxBullets, int fireRate, int reloadRate, float knockBack, float rumble,float recoilMult, float minRecoil, float speed, String fire, String empty, String reload, Effect effect, Sprite sprite, Vector2 offset) {
        this.bullet = bullet;
        this.projectiles = projectiles;
        this.maxBullets = maxBullets;
        this.fireRate = fireRate;
        this.reloadRate = reloadRate;
        this.knockBack = knockBack;
        this.rumble = rumble;
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

        public final Gun GUN;
        public final Entity ENTITY;
        private int bullets;
        private int fireTimer;
        private int reloadTimer;

        public float inaccuracy = 0;

        private Instance(Entity entity) {
            bullets = maxBullets;
            fireTimer = 0;
            reloadTimer = 0;
            ALL_INSTANCES.add(this);
            ENTITY = entity;
            GUN = Gun.this;
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
            Vector2 pos = ENTITY.getPos();
            float facing = (float)ENTITY.getFacing();
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
                    Effect.addEffect(EFFECT, 5, ENTITY.getPos(), (float) Math.toDegrees(ENTITY.getFacing() + Math.PI), 2);
                    bullets--;
                    for(int i = 0; i < projectiles; i ++) {
                        bullet.shoot(ENTITY, ENTITY.getPos(), ENTITY.getFacing(),inaccuracy);
                    }
                    fire.play();
                    ENTITY.POS.addVel((float) (ENTITY.getFacing() + Math.PI), knockBack);
                    ENTITY.FACE.recoil(recoilMult, minRecoil);
                    if(ENTITY instanceof Player) {
                        GameLevel.SCREEN_SHAKER.rumble(rumble,5);
                    }
                }
            }
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
