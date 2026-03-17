package WagonFortShootout.java.world;

import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.framework.image.Effect;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.HashSet;

public class SpacialHash {

    private final HashMap<GridPoint2, Cell> cells = new HashMap<GridPoint2, Cell>();
    private final Vector2 lowerBound;
    private final Vector2 upperBound;
    private final int xAxisCells;
    private final int yAxisCells;

    public SpacialHash(Vector2 lowerBound, Vector2 upperBound, int xAxisCells, int yAxisCells) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.xAxisCells = xAxisCells;
        this.yAxisCells = yAxisCells;
        for(int x = 0; x < xAxisCells; x ++) {
            for(int y = 0; y < yAxisCells; y++) {
                cells.put(new GridPoint2(x, y), new Cell());
            }
        }
    }

    public float cellWidth() {
        return (upperBound.x - lowerBound.x) / xAxisCells;
    }

    public float cellHeight() {
        return (upperBound.y - lowerBound.y) / yAxisCells;
    }

    public void add(Hitbox hitbox) {
        Rectangle bounds = hitbox.getBoundingBox();
        float halfWidth = bounds.width / 2;
        float halfHeight = bounds.height / 2;
        Vector2 pos = bounds.getCenter(new Vector2());
        GridPoint2 upperBounds = getIndex(new Vector2(pos.x + halfWidth, pos.y + halfHeight));
        GridPoint2 lowerBounds = getIndex(new Vector2(pos.x - halfWidth, pos.y - halfHeight));
        hitbox.gridBounds.upper = upperBounds;
        hitbox.gridBounds.lower = lowerBounds;
        for (int x = lowerBounds.x; x <= upperBounds.x; x++) {
            for (int y = lowerBounds.y; y <= upperBounds.y; y++) {
                cells.get(new GridPoint2(x, y)).add(hitbox);
            }
        }
    }

    public void remove(Hitbox hitbox) {
        GridPoint2 upperBounds = hitbox.gridBounds.upper;
        GridPoint2 lowerBounds = hitbox.gridBounds.lower;
        for(int x = lowerBounds.x; x <= upperBounds.x; x++) {
            for(int y = lowerBounds.y; y <= upperBounds.y; y++) {
                cells.get(new GridPoint2(x, y)).remove(hitbox);
            }
        }
    }

    public HashSet<Hitbox> query(Hitbox hitbox) {
        HashSet<Hitbox> hitboxes = new HashSet<Hitbox>();
        GridPoint2 upperBounds = hitbox.gridBounds.upper;
        GridPoint2 lowerBounds = hitbox.gridBounds.lower;
        for(int x = lowerBounds.x; x <= upperBounds.x; x++) {
            for (int y = lowerBounds.y; y <= upperBounds.y; y++) {
                hitboxes.addAll(cells.get(new GridPoint2(x, y)).query());
            }
        }
        return hitboxes;
    }

    public HashSet<Hitbox> query(Vector2 pos) {
        return cells.get(getIndex(pos)).hitboxes;
    }

    public HashSet<Hitbox> query(GridPoint2 point) {
        return cells.get(point).query();
    }

    public HashSet<Hitbox> query(Vector2 pos, float width, float height) {
        HashSet<Hitbox> hitboxes = new HashSet<Hitbox>();
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        GridPoint2 upperBounds = getIndex(new Vector2(pos.x + halfWidth, pos.y + halfHeight));
        GridPoint2 lowerBounds = getIndex(new Vector2(pos.x - halfWidth, pos.y - halfHeight));
        for(int x = lowerBounds.x; x < upperBounds.x; x++) {
            for(int y = lowerBounds.y; y < upperBounds.y; y++) {
                hitboxes.addAll(cells.get(new GridPoint2(x, y)).query());
            }
        }
        return hitboxes;
    }

    public boolean hasPoint(GridPoint2 point) {
        return cells.containsKey(point);
    }

    public void update(Hitbox hitbox) {
        remove(hitbox);
        add(hitbox);
    }

    public GridPoint2 getIndex(Vector2 pos) {
        float thingx = Math.clamp((pos.x - lowerBound.x) / (upperBound.x - lowerBound.x), 0, 1);
        int ix = (int)Math.floor(thingx * (xAxisCells));
        float thingy = Math.clamp((pos.y - lowerBound.y) / (upperBound.y - lowerBound.y), 0, 1);
        int iy = (int)Math.floor(thingy * (yAxisCells));
        if (ix >= xAxisCells) {
            ix = xAxisCells - 1;
        }
        if (iy >= yAxisCells) {
            iy = yAxisCells - 1;
        }
        return new GridPoint2(ix, iy);
    }

    private class Cell {

        private final HashSet<Hitbox> hitboxes = new HashSet<Hitbox>();

        public Cell() {
        }

        private void remove(Hitbox hitbox) {
            hitboxes.remove(hitbox);
        }

        private void add(Hitbox hitbox) {
            hitboxes.add(hitbox);
        }

        public HashSet<Hitbox> query() {
            return new HashSet<Hitbox>(hitboxes);
        }
    }

}
