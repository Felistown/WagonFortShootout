package WagonFortShootout.java.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Input {

    public static int last_key_down;
    public static final InputAdapter input = new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
          //TODO do player inputs like this
            return super.keyDown(keycode);
        }
    };

    public static void init() {
        JsonReader reader = new JsonReader();
        JsonValue file = reader.parse(Gdx.files.internal("data/keybinds.json"));
        JsonValue data;
        if(file.hasChild("custom")) {
            data = file.getChild("custom");
        } else {
            data = file.getChild("default");
        }
    }

    public static boolean forward() {
        return Gdx.input.isKeyPressed(Key.FORWARD.num);
    }

    public void set(Key k) {
        InputAdapter a = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return super.keyDown(keycode);
            }
        };

    }

    public static void reset() {
        JsonReader reader = new JsonReader();
        JsonValue file = reader.parse(Gdx.files.internal("data/keybinds.json"));
        if(file.hasChild("custom")) {
            file.remove("custom");
        }
        init();
    }

    public enum Key {
        FORWARD(51);

        private int num;

        private Key(int num) {
            this.num = num;
        }
    }
}
