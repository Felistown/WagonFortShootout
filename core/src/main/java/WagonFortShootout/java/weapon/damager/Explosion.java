package WagonFortShootout.java.weapon.damager;

import WagonFortShootout.java.entity.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Explosion {

    private static final float WIDTH = 0.25f;
    private static final int LIFETIME = 5;
    private static final Color COLOUR = new Color(255, 214, 0, 1);

    public final Bullet shrapnel;
    public float SPREAD;
    public int projectiles;

    public Explosion(int damage, float weight, float Spread, int projectiles,int piercing, float dist) {
        this.SPREAD = Spread;
        this.projectiles = projectiles;
        shrapnel = new Bullet(damage, weight, 0, piercing);
        shrapnel.dist = dist;
    }

    public Explosion(Bullet bullet, float spread, int projectiles) {
        shrapnel = bullet;
        SPREAD = spread;
        this.projectiles = projectiles;
    }

    public void explode(Entity entity, Vector2 pos, float face) {
        for(int i = 0; i < projectiles; i++) {
           shrapnel.shoot(entity, pos, (float) (face + (Math.random() * SPREAD) - (float)SPREAD / 2), 0);
        }
    }

    public static Explosion readJson(JsonValue value) {
        Bullet shrapnel = Bullet.readJson(value.get("shrapnel"));
        shrapnel.dist = value.getFloat("range");
        return new Explosion(shrapnel, value.getFloat("spread"), value.getInt("projectiles"));
    }
}
