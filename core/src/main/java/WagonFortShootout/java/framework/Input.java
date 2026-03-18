package WagonFortShootout.java.framework;

import WagonFortShootout.java.GameLevel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Input extends InputAdapter{

    public static InputAdapter adapter = new Input();

    public static int last_key_down;
    public static Cursor moving = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("image/cursor_moving.png")),15,15);
    public static Cursor set = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("image/cursor.png")),15,15);
    public static Vector2 offset = new Vector2(15,15);

    @Override
    public boolean scrolled(float amountX, float amountY) {
        GameLevel.cam.zoom += amountY * 0.1f;
        if(GameLevel.cam.zoom < 0.1) {
            GameLevel.cam.zoom = 0.1f;
        }
        return super.scrolled(amountX, amountY);
    }

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

    public static void tick() {
        if(GameLevel.player.FACE.isMoving()) {
            Gdx.graphics.setCursor(moving);
        } else {
            Gdx.graphics.setCursor(set);
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
