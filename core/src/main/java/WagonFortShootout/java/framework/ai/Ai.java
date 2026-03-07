package WagonFortShootout.java.framework.ai;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.ai.types.ControllableAi;
import WagonFortShootout.java.framework.ai.types.gunEnemyAi;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;

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

    //ticked by entity
    public abstract void tick();

    //runs once every num ai ticks
    public abstract void update();

    public static void tickAll() {
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
        int prev = ALL_AI.size();
        ALL_AI.removeAll(toRemove);
        int rem = prev - ALL_AI.size();
        if(rem > 0) {
            Gdx.app.log("Ai", "Removed " + rem + " Ai.");
        }
        toRemove.clear();
    }

    public static Ai fromType(String name, Entity entity) {
        return Type.fromString(name).FACTORY.apply(entity);
    }

    public enum Type {
        GUN_ENEMY("gun_enemy", gunEnemyAi::new),
        CONTROLLABLE("controllable", ControllableAi::new);

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
