package WagonFortShootout.java.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Beam {

    private static final ArrayList<Beam> ALL_BEAMS = new ArrayList<Beam>();

    private final Vector2 TO;
    private final Vector2 FROM;
    private int lifetime;
    private final float width;
    private final Color colour;

    public static void beam(Vector2 from, Vector2 to, float width, int lifetime, Color colour) {
        Beam beam = new Beam(from, to,width, lifetime, colour);
        ALL_BEAMS.add(beam);
    }

    private Beam(Vector2 from, Vector2 to, float width, int lifetime, Color colour) {
        FROM = from;
        TO = to;
        this.width = width;
        this.lifetime = lifetime;
        this.colour = colour;
    }

    public static void renderAll(Matrix4 matrix) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i < ALL_BEAMS.size(); i ++) {
            Beam b = ALL_BEAMS.get(i);
            shapeRenderer.setColor(b.colour);
            shapeRenderer.rectLine(b.FROM, b.TO, b.width);
            b.lifetime --;
            if(b.lifetime <= 0) {
                ALL_BEAMS.remove(b);
                i--;
            }
        }
        shapeRenderer.end();
    }
}
