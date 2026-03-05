package WagonFortShootout.java;

import WagonFortShootout.java.framework.Input;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override

    public void create() {
        Gdx.input.setInputProcessor(Input.input);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setScreen(new GameLevel());
    }

}
