package WagonFortShootout.java.framework.ai;

import WagonFortShootout.java.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;

public abstract class Ai {

    private static final ArrayList<Ai> ALL_AI = new ArrayList<Ai>();
    private static final HashSet<Ai> toRemove = new HashSet<Ai>();
    private static final HashSet<Ai> toAdd = new HashSet<Ai>();
    private static int index = 0;

    protected final Entity entity;

    public Ai(Entity entity) {
        this.entity = entity;
        toAdd.add(this);
    }

    public void remove() {
        toRemove.add(this);
    }

    //runs every tick
    public void tick() {

    }

    //runs once every num ai ticks
    public void update() {

    }

    public static void tickAll() {
        ALL_AI.forEach(Ai::tick);
        if(!ALL_AI.isEmpty()) {
            Ai toTick = ALL_AI.get(index);
            if (!toRemove.contains(toTick)) {
                toTick.update();
            }
            index++;
        } else {
            updateList();
        }
        if (index >= ALL_AI.size()) {
            index = 0;
            updateList();
        }
    }

    public static void updateList() {
        ALL_AI.addAll(toAdd);
        toAdd.clear();
        ALL_AI.removeAll(toRemove);
        toRemove.clear();
    }

    public static Ai fromType(String name, Entity entity) {
        return Type.fromString(name).FACTORY.apply(entity);
    }

    public enum Type {
        GUN_ENEMY("gun_enemy", gunEnemyAi::new);

        public final String NAME;
        public final Function<Entity, Ai> FACTORY;

        Type(String name, Function<Entity, Ai> factory) {
            NAME = name;
            FACTORY = factory;
        }

        public static Ai.Type fromString(String name) {
            Type[] all = Type.values();
            for(Type t: all) {
                if(t.NAME.equals(name)) {
                    return t;
                }
            }
            return null;
        }
    }
}
