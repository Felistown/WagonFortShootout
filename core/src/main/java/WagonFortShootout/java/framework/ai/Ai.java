package WagonFortShootout.java.framework.ai;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.ai.pathfinding.Pathfinder;
import WagonFortShootout.java.framework.ai.types.ControllableAi;
import WagonFortShootout.java.framework.ai.types.gunEnemyAi;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
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
    protected final StateMachine STATE;
    private Pathfinder.Path path;
    private int cooldown = 0;

    public Ai(Entity entity, StateMachine stateMachine) {
        this.entity = entity;
        STATE = stateMachine;
        toAdd.add(this);
    }

    public void remove() {
        toRemove.add(this);
    }

    //ticked by entity
    public void tick() {
        cooldown = Math.max(0, cooldown - 1);
        if(path != null) {
            path.follow(entity);
        }
    }

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
        ALL_AI.removeAll(toRemove);
        toRemove.forEach(Ai::onRemove);
        toRemove.clear();
    }

    public boolean moveable() {
        return cooldown <= 0;
    }

    public void goTo(Vector2 target) {
        if(cooldown <= 0) {
            cooldown = 300;
            path = new Pathfinder(entity.getPos(), target, entity.HITBOX).findPath();
            if(path != null) {
                path.next();
            }
        }
    }

    protected void onRemove() {
        Gdx.app.log("Ai", "Ai removed");
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

    public boolean hasEnded() {
        //TODO not working, never updating when end of path reached
        if(path == null) {
            return true;
        }
        return path.hasEnded();
    }
}
