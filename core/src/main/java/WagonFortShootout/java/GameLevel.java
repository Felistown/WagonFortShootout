package WagonFortShootout.java;

import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.Enemy;
import WagonFortShootout.java.entity.entities.Player;
import WagonFortShootout.java.effects.Beam;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** First screen of the application. Displayed after the application is created. */
public class GameLevel implements Screen {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static Viewport viewport = new FillViewport(WIDTH, HEIGHT);
    public static Vector2 mouse = new Vector2();
    private static final SpriteBatch SPRITE_BATCH = new SpriteBatch();

    @Override
    public void show() {
        // Prepare your screen here.
        new Player(new Vector2(25,25));
        new Enemy(new Vector2(30,30));
        new Enemy(new Vector2(50,30));
        new Enemy(new Vector2(30,50));
        new Enemy(new Vector2(80,30));
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        tick();
        draw();
    }

    public void input() {
        Entity.tickAll();
    }

    public void tick() {
        Entity.tickAll();
    }

    public void draw() {
        ScreenUtils.clear(new Color(0.28f, 0.43f,0.21f, 1));
        viewport.apply();
        SPRITE_BATCH.setProjectionMatrix(viewport.getCamera().combined);
        SPRITE_BATCH.begin();
        Entity.drawAll(SPRITE_BATCH);
        Effect.renderAll(SPRITE_BATCH);
        SPRITE_BATCH.end();
        Beam.renderAll(viewport.getCamera().combined);
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
        // Destroy screen's assets here.
    }
}
