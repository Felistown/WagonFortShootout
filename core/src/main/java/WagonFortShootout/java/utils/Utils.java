package WagonFortShootout.java.utils;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.entity.Hitbox;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Utils {



    public static double distFrom(Entity e, Entity o) {
        return e.getPos().dst(o.getPos());
    }

    public static double distFrom(Entity e, Hitbox o) {
        return e.getPos().dst(new Vector2(o.POLYGON.getX(), o.POLYGON.getY()));
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

    public static boolean los(Entity e, Entity o) {
        Hitbox[] all = Utils.closetHitBox(e);
        for(Hitbox h: all) {
            if(h != e.HITBOX && !(o.mount != null && h == o.mount.HITBOX)) {
                if(h.rayIntersection(e.getPos(), e.getPos().add(Mth.toVec(e.getFacing(), 400)), null)) {
                    return h == o.HITBOX;
                }
            }
        }
        return false;
    }

    public static float dist(Entity e, Entity o) {
        return e.getPos().dst(o.getPos());
    }

    public static <K, V> K findKey(HashMap<K, V> set, V value) {
        Set<K> keys = set.keySet();
        for(K key: keys) {
            if(set.get(key) == value) {
                return key;
            }
        }
        return null;
    }

}
