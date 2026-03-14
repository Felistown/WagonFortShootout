package WagonFortShootout.java;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.EntityLoader;
import WagonFortShootout.java.entity.entities.*;
import WagonFortShootout.java.framework.Input;
import WagonFortShootout.java.framework.ai.Team;
import WagonFortShootout.java.framework.gui.Gui;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.gui.HealthBar;
import WagonFortShootout.java.framework.image.Sprite;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.weapon.Gun;
import WagonFortShootout.java.world.Object;
import WagonFortShootout.java.framework.gui.ScreenShaker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** First screen of the application. Displayed after the application is created. */
public class GameLevel implements Screen {
    public static final int WIDTH = 100;
    public static final int HEIGHT =100;
    public static Viewport viewport = new FitViewport(WIDTH, HEIGHT);
    public static Vector2 mouse = new Vector2();
    private static final SpriteBatch SPRITE_BATCH = new SpriteBatch();
    public static final ScreenShaker SCREEN_SHAKER = new ScreenShaker(viewport.getCamera(),HEIGHT,WIDTH);
    private Texture BACKROUND;
    public Gui gui;

    public static Entity player;

    @Override
    public void show() {
        // Prepare your screen here.
        //EntityLoader.get("horse",new Vector2(86,94), Team.neutral() );

        Team enemy = new Team((byte)2);
        Team friend = new Team((byte)3);

        Sprite backround = new Sprite("grass", -1, 100,100);
        backround.setSize(WIDTH, HEIGHT);
        backround.setCentre((float) WIDTH /2, (float) HEIGHT /2);
        //new Tank(new Vector2(92,92), Team.neutral());
       // EntityLoader.get("maxim", new Vector2(86,94), Team.neutral());


        player = EntityLoader.get("player", new Vector2(95,95), friend);
       // new gunEnemy(new Vector2(10,10), enemy);
        //Horse hors = new Horse(new Vector2(95,95), Team.unaffiliated());
        Object.objectInstance("cart", new Vector2(88,88), 45);
        Object.objectInstance("cart", new Vector2(12,12), 45);
        /*
        Object.objectInstance("cart", new Vector2(86,90), 45);
        Object.objectInstance("cart", new Vector2(84,92), 45);
        Object.objectInstance("cart", new Vector2(90,86), 45);
        Object.objectInstance("cart", new Vector2(92,84), 45);

         */

        EntityLoader.get("tank", new Vector2(80,80), Team.neutral());

        /*
        new gunEnemy(new Vector2(99,1));
        new gunEnemy(new Vector2(1,99));
        new gunEnemy(new Vector2(1,1));
        new gunEnemy(new Vector2(1,99));

         */

/*
        for(int i = 0; i < 10; i++) {
           EntityLoader.get("canny_soldier", new Vector2(99,999), friend);

        }


 */
        for(int i = 0; i < 10; i++) {
            EntityLoader.get("uncanny_soldier", new Vector2(1, 1), enemy);
        }



/*
        for(int i = 0; i < 50; i++) {
            Vector2 pos = Mth.randomVec(new Vector2(2,2), 98);
            float angle = (float)(Math.random() * 180);
            Object.objectInstance("cart", pos, angle);
        }


 */

        gui = new Gui(player);
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        HealthBar.tick();
        input();
        tick();
        draw();
    }

    public void input() {
        Entity.tickAll();
    }

    public void tick() {
        Input.tick();
        SCREEN_SHAKER.tick();
        Ai.tickAll();
        Entity.tickAll();
        Gun.Instance.tickAll();
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        SPRITE_BATCH.setProjectionMatrix(viewport.getCamera().combined);
        SPRITE_BATCH.begin();

        Sprite.drawAll(SPRITE_BATCH);
        gui.render(SPRITE_BATCH);

        SPRITE_BATCH.end();
        mouse.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouse);
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        viewport.update(width, height, true);
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        Gun.disposeAll();
        SPRITE_BATCH.dispose();
        Sprite.dispose();
        // Destroy screen's assets here.
    }
}
