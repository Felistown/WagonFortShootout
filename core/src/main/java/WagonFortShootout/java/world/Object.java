package WagonFortShootout.java.world;

import WagonFortShootout.java.framework.data.HitResult;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.entity.HitboxHolder;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.HashSet;

public class Object {

    private static HashMap<String, Object> ALL_OBJECTS = new HashMap<String, Object>();

    private final String texture;
    private final Polygon polygon;
    private final int resistance;
    private final float length;
    private final float height;

    static {
        JsonReader reader = new JsonReader();
        FileHandle dir = Gdx.files.internal("data/objects/");
        for(FileHandle file: dir.list(".json")) {
            initObject(reader.parse(file), file.nameWithoutExtension());
        }
        reader.stop();
    }

    public Object(String texture, Polygon polygon, int resistance, float length, float height) {
        this.texture = texture;
        this.polygon = polygon;
        this.resistance = resistance;
        this.length = length;
        this.height = height;
    }

    private static void initObject(JsonValue current, String name) {
        JsonValue polygon = current.get("polygon");
        float height = polygon.getFloat("height");
        float length = polygon.getFloat("length");
        ALL_OBJECTS.put(name, new Object(current.getString("texture"),Mth.rectange(length, height), current.getInt("resistance"), length, height));
        Gdx.app.log("Objects", "Loaded object: " + name);
    }

    public static Instance objectInstance(String name, Vector2 pos, float rotation) {
        if(ALL_OBJECTS.containsKey(name)) {
            return ALL_OBJECTS.get(name).new Instance(pos, rotation);
        } else {
            throw new RuntimeException("No such object \"" + name + " \".");
        }
    }

    public void onHit(HitResult data) {
        data.piercing.sub(resistance);
    }


    public class Instance implements HitboxHolder{

        private static HashSet<Instance> ALL_INSTANCES = new HashSet<Instance>();

        private final Sprite sprite;
        private final Hitbox hitbox;
        private final Vector2 pos;

        private Instance(Vector2 pos, float rotation) {
            this.pos = pos;
            hitbox = Hitbox.Builder.polygon(new Polygon(polygon.getVertices().clone())).build(this, Object.this::onHit);
            hitbox.setRotation(rotation);
            hitbox.setAnchored(true);
            hitbox.setPosition(pos);
            sprite = new Sprite(texture, 0, length, height);
            sprite.setCentre(pos);
            sprite.setRotationRad((float)Math.toRadians(rotation));
            ALL_INSTANCES.add(this);
            float[] v = hitbox.POLYGON.getTransformedVertices();
            Vector2[] vertices = new Vector2[v.length / 2];
            int index = 0;
            for(int i = 0; i + 1 < v.length; i += 2) {
                vertices[index] = new Vector2(v[i], v[i + 1]);
                //Effect.addEffect(new Effect(new Texture("image/missing_texture.png"),0.2f,0.2f), 1000, vertices[index], 0);
                index++;
            }
        }

        private void remove() {
            hitbox.remove();
            sprite.remove();
            ALL_INSTANCES.remove(this);
        }

        public static Object.Instance[] getAll() {
            return ALL_INSTANCES.toArray(new Instance[0]);
        }

        public Polygon hitbox() {
            return hitbox.POLYGON;
        }
    }


}
