package WagonFortShootout.java.framework;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Cam extends OrthographicCamera {

    private Vector3 pos;
    private static int time = 0;
    private static float power = 0f;

    public Cam(Vector2 pos) {
        super();
        this.pos = new Vector3(pos,0);
    }

    public void setPos(Vector2 pos) {
        if(viewportHeight * zoom > GameLevel.HEIGHT) {
            zoom /= (viewportHeight * zoom / GameLevel.HEIGHT);
        }
        if(viewportWidth * zoom > GameLevel.WIDTH) {
            zoom /= (viewportWidth * zoom / GameLevel.WIDTH);
        }
        float hw = (viewportWidth /2) * zoom;
        float hh = (viewportHeight / 2) * zoom;
        if(pos.y + hh  > GameLevel.HEIGHT) {
            pos.y = GameLevel.HEIGHT - hh;
        } else if(pos.y -hh < 0) {
            pos.y = hh;
        }
        if(pos.x + hw > GameLevel.WIDTH) {
            pos.x = GameLevel.WIDTH - hw;
        } else if(pos.x - hw < 0) {
            pos.x = hw;
        }
        this.pos = new Vector3(pos, 0);

    }

    public void rumble(float rumblePower, int rumbleLength) {
        power = rumblePower;
        time = rumbleLength;
    }

    public void tick() {
        time = Math.max(time - 1, 0);
        if(time <= 0) {
            position.set(pos);
        } else {
            translate(new Vector3(Mth.randomVec(-power/2,power),0));
        }
    }
}
