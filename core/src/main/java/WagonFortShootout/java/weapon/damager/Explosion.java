package WagonFortShootout.java.weapon.damager;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.weapon.damager.custom.ProjectileTypes;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class Explosion {

    private static final float WIDTH = 0.25f;
    private static final int LIFETIME = 5;
    private static final Color COLOUR = new Color(255, 214, 0, 1);

    public final Projectile shrapnel;
    public float SPREAD;
    public int projectiles;

    public Explosion(JsonValue value) {
        shrapnel = ProjectileTypes.get(value.get("shrapnel"));
        SPREAD = value.getFloat("spread");
        projectiles = value.getInt("projectiles");
    }

    public void explode(Entity entity, Vector2 pos, float face) {
        for(int i = 0; i < projectiles; i++) {
           shrapnel.shoot(entity, pos, (float) (face + (Math.random() * SPREAD) - (float)SPREAD / 2), 0);
        }
    }

}
