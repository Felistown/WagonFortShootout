package WagonFortShootout.java.utils;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.world.Hitbox;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Utils {



    public static double distFrom(Entity e, Entity o) {
        return e.getPOS().pos().dst(o.getPOS().pos());
    }

    public static double distFrom(Entity e, Hitbox o) {
        return e.getPOS().pos().dst(new Vector2(o.POLYGON.getX(), o.POLYGON.getY()));
    }

    public static Entity[] closetEntity(Entity entity) {
        Entity[] set = Entity.getAllEntities().toArray(Entity[]::new);
        Arrays.sort(set, Comparator.comparingDouble(a -> distFrom(entity, (Entity) a)));
        return set;
    }

    public static Hitbox[] closetHitBox(Entity entity) {
        Hitbox[] set = Hitbox.getAllHitboxes();
        Arrays.sort(set, Comparator.comparingDouble(a -> distFrom(entity, a)));
        return set;
    }

    public static <T> ArrayList<T> toArraylist(T[] array) {
        ArrayList<T> arrayList = new ArrayList<T>();
        if(array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                arrayList.set(i, array[i]);
            }
        }
        return arrayList;
    }
}
