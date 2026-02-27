package WagonFortShootout.java.weapon;

import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.HashMap;
import java.util.HashSet;

public class Gun {

    private static final HashMap<String, Gun> ALL_GUNS = new HashMap<String, Gun>();
    private static final float DIST = 150;
    private static final float WIDTH = 0.25f;
    private static final int LIFETIME = 5;
    private static final Color COLOUR = new Color(255, 214, 0, 1);

    private final int damage;
    private final int maxBullets;
    private final int fireRate;
    private final int reloadRate;
    private final Sound fire;
    private final Sound empty;
    private final Sound reload;
    private final Effect EFFECT;
    private final float knockBack;
    private final float recoilMult;
    private final float minRecoil;

    public static void init() {
        Gun rifle = new Gun(1, 8, 50, 50,1,1.2f, 0.5f, "rifle_fire.mp3","rifle_empty.mp3","rifle_reload.mp3",new Effect(new Texture("muzzle_flash.png"),3,3));
        ALL_GUNS.put("rifle", rifle);
    }

    public static Gun.Instance getGun(String name) {
        if(ALL_GUNS.containsKey(name)) {
            return ALL_GUNS.get(name).new Instance();
        } else {
            throw new RuntimeException("No such gun \"" + name + "\".");
        }
    }

    private Gun(int damage, int maxBullets, int fireRate, int reloadRate, float knockBack, float recoilMult, float minRecoil, String fire, String empty, String reload, Effect effect) {
        this.damage = damage;
        this.maxBullets = maxBullets;
        this.fireRate = fireRate;
        this.reloadRate = reloadRate;
        this.knockBack = knockBack;
        this.recoilMult = recoilMult;
        this.minRecoil = minRecoil;
        this.fire = Gdx.audio.newSound(Gdx.files.internal(fire));
        this.empty = Gdx.audio.newSound(Gdx.files.internal(empty));
        this.reload = Gdx.audio.newSound(Gdx.files.internal(reload));
        this.EFFECT = effect;
    }

    public class Instance {

        private static HashSet<Instance> ALL_INSTANCES = new HashSet<Instance>();

        private int bullets;
        private int fireTimer;
        private int reloadTimer;

        private Instance() {
            bullets = maxBullets;
            fireTimer = 0;
            reloadTimer = 0;
            ALL_INSTANCES.add(this);
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

        public void shoot(Entity entity) {
            if(fireTimer <= 0 && reloadTimer <= 0) {
                fireTimer += fireRate;
                if(bullets <= 0) {
                    empty.play();
                } else {
                    bullets--;
                    Vector2 pos = entity.getPOS().pos();
                    float face = (float) entity.getFACE().getFacing();
                    entity.getPOS().addVel((float) (face + Math.PI), knockBack);
                    entity.getFACE().recoil(recoilMult, minRecoil);
                    Effect.addEffect(EFFECT, 5, pos, (float) Math.toDegrees(face + Math.PI));
                    fire.play();
                    Ray detection = new Ray(new Vector3(pos, 0), new Vector3(-(float) Math.cos(face), -(float) Math.sin(face), 0).scl(DIST));
                    Entity hitEntity = null;
                    Vector2 temp = Mth.toVec(face, DIST).add(pos);
                    Vector3 hitPos = new Vector3(temp.x, temp.y, 0);
                    for (Entity e : Entity.getAllEntities()) {
                        Circle hitbox = e.getHitbox();
                        if (e != entity && Intersector.intersectRaySphere(detection, new Vector3(hitbox.x, hitbox.y, 0), hitbox.radius, hitPos)) {
                            hitEntity = e;
                            break;
                        }
                    }
                    Beam.beam(pos, new Vector2(hitPos.x, hitPos.y), WIDTH, LIFETIME, COLOUR);
                    if (hitEntity != null) {
                        hitEntity.onHit(damage);
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
    }
}
