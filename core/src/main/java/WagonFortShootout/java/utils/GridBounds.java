package WagonFortShootout.java.utils;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class GridBounds {

    public GridPoint2 lower;
    public GridPoint2 upper;

    public GridBounds(GridPoint2 lower, GridPoint2 upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public GridBounds() {
        this.lower = new GridPoint2();
        this.upper = new GridPoint2();
    }

}
