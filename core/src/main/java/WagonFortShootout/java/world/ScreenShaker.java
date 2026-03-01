package WagonFortShootout.java.world;

import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ScreenShaker {

    private static int time = 0;
    private static float power = 0f;
    private Camera cam;
    private float worldHeight;
    private float worldWidth;


    public ScreenShaker(Camera cam,float worldHeight, float worldWidth) {
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        this.cam = cam;
    }

    public void rumble(float rumblePower, int rumbleLength) {
        power = rumblePower;
        time = rumbleLength;
    }

    public void tick() {
        time = Math.max(time - 1, 0);
        if(time == 1) {
            cam.position.set(worldWidth / 2f, worldHeight/2f, 0);
        } else if(time > 0) {
            cam.position.set(worldWidth / 2f, worldHeight/2f, 0);
            cam.translate(new Vector3(Mth.randomVec(-power/2,power),0));
        }
    }
}
