package WagonFortShootout.java.world;

import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.HashSet;

public class Object {

    private static HashMap<String, Object> ALL_OBJECTS = new HashMap<String, Object>();

    private final Polygon polygon;
    private final Texture texture;
    private final int resistance;
    private final float length;
    private final float height;

    public Object(Texture texture, Polygon polygon, int resistance, float length, float height) {
        this.texture = texture;
        this.polygon = polygon;
        this.resistance = resistance;
        this.length = length;
        this.height = height;
    }

    public static void init() {
        JsonReader reader = new JsonReader();
        JsonValue file = reader.parse(Gdx.files.internal("data/object.json"));
        JsonValue current = file.child();
        do {
            JsonValue polygon = current.get("polygon");
            float height = polygon.getFloat("height");
            float length = polygon.getFloat("length");
            ALL_OBJECTS.put(current.name, new Object(new Texture(current.getString("texture")),Mth.rectange(length, height), current.getInt("resistance"), length, height));
            current = current.next;
        } while (current != null);
    }

    public static Instance objectInstance(String name, Vector2 pos, float rotation) {
        if(ALL_OBJECTS.containsKey(name)) {
            return ALL_OBJECTS.get(name).new Instance(pos, rotation);
        } else {
            throw new RuntimeException("No such object \"" + name + " \".");
        }
    }

    public class Instance {

        private static HashSet<Instance> ALL_INSTANCES = new HashSet<Instance>();

        private final Sprite sprite;
        private final Hitbox hitbox;
        private final Vector2 pos;

        private Instance(Vector2 pos, float rotation) {
            this.pos = pos;
            hitbox = new Hitbox(new Polygon(polygon.getVertices().clone()), pos, resistance);
            hitbox.setRotation(rotation);
            hitbox.anchored = true;
            sprite = new Sprite(texture);
            sprite.setSize(length, height);
            sprite.setCenter(pos.x, pos.y);
            sprite.setOriginCenter();
            sprite.setRotation(rotation);
            ALL_INSTANCES.add(this);
            float[] v = hitbox.POLYGON.getTransformedVertices();
            Vector2[] vertices = new Vector2[v.length / 2];
            int index = 0;
            for(int i = 0; i + 1 < v.length; i += 2) {
                vertices[index] = new Vector2(v[i], v[i + 1]);
                Effect.addEffect(new Effect(new Texture("image/missing_texture.png"),0.2f,0.2f), 1000, vertices[index], 0);
                index++;
            }
        }

        public static void renderAll(SpriteBatch spriteBatch) {
            ALL_INSTANCES.forEach(o -> o.render(spriteBatch));
        }

        private void render(SpriteBatch spriteBatch) {
            sprite.draw(spriteBatch);
        }

        private void remove() {
            hitbox.remove();
            ALL_INSTANCES.remove(this);
        }
    }
}
