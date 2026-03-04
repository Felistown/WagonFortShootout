package WagonFortShootout.java.framework.ai.pathfinding;

import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.world.Hitbox;
import WagonFortShootout.java.world.Object;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Pathfinder {

    //TODO implement pathfinding, this already identifies very moveable point
    public static void a() {
        Object.Instance[] all = Object.Instance.getAll();
        for(int x = 0; x < 100; x++) {
            for(int y = 0; y < 100; y++) {
                Vector2 pos = new Vector2(x,y);
                boolean t = true;
                for(Object.Instance o:all) {
                    if(o.hitbox().contains(pos)) {
                        t = false;
                        break;
                    }
                }
                if(t) {
                    //Effect.addEffect(new Effect(new Texture("image/missing_texture.png"),0.5f,0.5f), 1000, pos, 0, 4);
                }
            }
        }
    }

}
