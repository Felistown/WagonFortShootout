package WagonFortShootout.java.entity;

import WagonFortShootout.java.entity.entities.*;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.utils.TriFunction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.function.BiFunction;

public class EntityLoader {

    private static final FileHandle DIRECTORY = Gdx.files.internal("data/entities/");
    private static final HashMap<String, JsonValue> entities = new HashMap<String, JsonValue>();

    static {
        JsonReader reader = new JsonReader();
        for(FileHandle file: DIRECTORY.list(".json")) {
            String name = file.nameWithoutExtension();
            Gdx.app.log("EntityLoader", "Loaded entity \"" + name + "\".");
            entities.put(name, reader.parse(file));
        }
        reader.stop();
    }


    public static Entity get(String name, Vector2 pos, Team team) {
        if(entities.containsKey(name)) {
            JsonValue entity = entities.get(name);
            String type = entity.getString("type");
            for(Loadable et: Loadable.values()) {
                if(et.name().toLowerCase().equals(type)) {
                    Gdx.app.log("EntityLoader", "Creating entity \"" + name + "\", of type " + type + ".");
                    return et.factory.apply(pos, entity, team);
                }
            }
        } else {
            for(Unloadable et: Unloadable.values()) {
                if(et.name().toLowerCase().equals(name)) {
                    Gdx.app.log("EntityLoader", "Creating entity \"" + name + "\".");
                    return et.factory.apply(pos, team);
                }
            }
        }
        throw new RuntimeException("Error whilst creating entity: " + name + ".");
    }


    public enum Loadable {
        GUN_ENTITY(GunEntity::new),
        PLAYER(Player::load),
        TANK(Tank::new),
        HORSE(Horse::new);

        private final TriFunction<Vector2, JsonValue, Team, Entity> factory;

        private Loadable(TriFunction<Vector2, JsonValue, Team, Entity> factory) {
            this.factory = factory;
        }
    }

    public enum Unloadable {
        MAXIM(Maxim::new);

        private final BiFunction<Vector2, Team, Entity> factory;

        private Unloadable(BiFunction<Vector2, Team, Entity> factory) {
            this.factory = factory;
        }
    }
}
