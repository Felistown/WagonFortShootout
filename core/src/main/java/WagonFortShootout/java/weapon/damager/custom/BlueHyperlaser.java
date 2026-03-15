package WagonFortShootout.java.weapon.damager.custom;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.entity.ConjoinedHitbox;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.weapon.damager.Ray;
import WagonFortShootout.java.world.Object;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

public class BlueHyperlaser extends Ray {

    private static final WagonFortShootout.java.framework.image.Beam outine = new WagonFortShootout.java.framework.image.Beam("effects/blue_hyperlaser", 3, 80, 0.25f);

    public BlueHyperlaser(JsonValue value) {
        super(value);
    }

    @Override
    public void onHit(HitResult data) {
        if(data.holder instanceof Object.Instance obj) {
            display2(obj.hitbox);
            obj.remove();
        } else if(data.holder instanceof Entity e) {
            display2(((Entity)data.holder).HITBOX);
            e.health = 0;
        }
    }

    private void display2(Hitbox hitbox) {
        if(hitbox instanceof ConjoinedHitbox h) {
            display(h);
            for(Hitbox e: h.getSubs()) {
                display(e);
            }
        } else {
            display(hitbox);
        }
    }

    private void display(Hitbox hitbox) {
        Vector2[] verticies = hitbox.getVertices();
        Vector2 last = verticies[verticies.length - 1];
        for (int i = 0; i < verticies.length; i++) {
            Vector2 current = verticies[i];
            outine.instance(last, current);
            last = current;
        }
    }
}
