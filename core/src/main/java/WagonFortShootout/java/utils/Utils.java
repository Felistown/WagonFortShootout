package WagonFortShootout.java.utils;

import WagonFortShootout.java.entity.Entity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class Utils {

    public static double distFrom(Entity e, Entity o) {
        return e.getPOS().pos().dst(o.getPOS().pos());
    }

    public static Entity[] byClosest(Entity entity) {
        Entity[] set = Entity.getAllEntities().toArray(Entity[]::new);
        Arrays.sort(set, Comparator.comparingDouble(a -> distFrom(entity, (Entity) a)));
        return set;

    }
}
