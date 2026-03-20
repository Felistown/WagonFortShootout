package WagonFortShootout.java.framework.input;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.framework.input.annotations.KeyInputEvent;
import WagonFortShootout.java.framework.input.annotations.InputEventSubscriber;
import WagonFortShootout.java.framework.input.annotations.MouseMoveEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Input extends InputAdapter {

    public static Cursor moving = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("image/cursor_moving.png")),15,15);
    public static Cursor set = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("image/cursor.png")),15,15);

    private static final HashMap<String, HashSet<MethodObject>> keySubs = new HashMap<String, HashSet<MethodObject>>();
    private static final HashSet<MethodObject> mouseSubs = new HashSet<MethodObject>();
    private static final Class[] keyArgs = new Class[]{KeyEvent.class};
    private static final Class[] mouseArgs = new Class[]{Vector2.class};

    public static InputAdapter adapter = new Input();

    private static Range range;

    static {
        range = Range.MENU;
        keySubs.put("forward", new HashSet<MethodObject>());
        keySubs.put("left", new HashSet<MethodObject>());
        keySubs.put("back", new HashSet<MethodObject>());
        keySubs.put("right", new HashSet<MethodObject>());
    }

    public static void setRange(Range range) {
        if(range != Range.ALL) {
            Input.range = range;
        } else {
            throw new RuntimeException("Cannot set range to ALL");
        }
    }

    public static void subscribe(Object object) {
        Class<?> c = object.getClass();
        if(c.isAnnotationPresent(InputEventSubscriber.class)) {
            Range range = Range.fromString(c.getAnnotation(InputEventSubscriber.class).range());
                for (Method m : c.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(KeyInputEvent.class)) {
                        Class[] args = m.getParameterTypes();
                        if (Arrays.equals(args, keyArgs)) {
                            String name = m.getAnnotation(KeyInputEvent.class).name();
                            if (keySubs.containsKey(name)) {
                                keySubs.get(name).add(new MethodObject(range,object, m));
                            } else {
                                throw new RuntimeException("No input found named \"" + name + "\".");
                            }
                        } else {
                            throw new RuntimeException("Key event methods must accept KeyEvents");
                        }
                    } else if(m.isAnnotationPresent(MouseMoveEvent.class)) {
                        Class[] args = m.getParameterTypes();
                        if (Arrays.equals(args, mouseArgs)) {
                            mouseSubs.add(new MethodObject(range,object, m));
                        } else {
                            throw new RuntimeException("Mousemove event methods must accept Vector2");
                        }
                    }
                }
        } else {
            throw new RuntimeException("Subscriber must be annotated with InputEventSubscriber");
        }
    }



    @Override
    public boolean scrolled(float amountX, float amountY) {
        GameLevel.cam.zoom += amountY * 0.1f;
        if(GameLevel.cam.zoom < 0.1) {
            GameLevel.cam.zoom = 0.1f;
        }
        return super.scrolled(amountX, amountY);
    }

    public static void tick() {
        if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            keySubs.get("forward").forEach(a -> a.invoke(range, KeyEvent.PRESS));
        }
        if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            keySubs.get("left").forEach(a -> a.invoke(range, KeyEvent.PRESS));
        }
        if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            keySubs.get("back").forEach(a -> a.invoke(range, KeyEvent.PRESS));
        }
        if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            keySubs.get("right").forEach(a -> a.invoke(range, KeyEvent.PRESS));
        }
        if(GameLevel.player.FACE.isMoving()) {
            Gdx.graphics.setCursor(moving);
        } else {
            Gdx.graphics.setCursor(set);
        }
    }

    public static void unsubscribe(Object object) {
        for(HashSet<MethodObject> set: keySubs.values()) {
            set.removeIf(e -> e.object == object);
        }
        mouseSubs.removeIf(e -> e.object == object);
    }

    private record MethodObject(Range range, Object object, Method method) {

        public void invoke(Range range, Object input) {
            if(this.range == range || range == Range.ALL) {
                try {
                    method.invoke(object, input);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static enum KeyEvent {
        PRESS,
        HOLD,
        RELEASE;
    }
    public static enum Range {
        GAME,
        MENU,
        ALL;

        public static Range fromString(String range) {
            for(Range r: Range.values()) {
                if(r.name().toLowerCase().equals(range)) {
                    return r;
                }
            }
            throw new RuntimeException("Invalid range: \"" + range + "\".");
        }
    }
}
