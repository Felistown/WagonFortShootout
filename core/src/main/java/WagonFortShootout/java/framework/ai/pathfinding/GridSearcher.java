package WagonFortShootout.java.framework.ai.pathfinding;

import com.badlogic.gdx.math.Vector2;

import java.util.function.Consumer;

public class GridSearcher {

    private final Vector2 centre;
    private final int maxSize;

    private int layer;
    private int step;
    private byte side;

    public GridSearcher(Vector2 centre, int maxSize) {
        this.centre = centre;
        this.maxSize = maxSize;
        side = 0;
        step = 0;
        layer = 0;
    }

    public GridSearcher resize(int maxSize) {
        GridSearcher other = new GridSearcher(centre, maxSize);
        other.side = this.side;
        other.step = this.step;
        other.layer = this.layer;
        return other;
    }

    public GridSearcher reposition(Vector2 centre) {
        GridSearcher other = new GridSearcher(centre, maxSize);
        other.side = this.side;
        other.step = this.step;
        other.layer = this.layer;
        return other;
    }

    public GridSearcher reposize(Vector2 centre, int maxSize) {
        GridSearcher other = new GridSearcher(centre, maxSize);
        other.side = this.side;
        other.step = this.step;
        other.layer = this.layer;
        return other;
    }

    public void forEachSquare(Consumer<Vector2> consumer) {
        while(hasNext()) {
            consumer.accept(nextSquare());
        }
    }

    public void forEachCircle(Consumer<Vector2> consumer) {
        while(hasNext()) {
            consumer.accept(nextCircle());
        }
    }

    public boolean hasNext() {
        return layer <= maxSize;
    }

    public Vector2 nextCircle() {
        while(hasNext()) {
            Vector2 next = nextSquare();
            if(next.dst2(centre) <= maxSize * maxSize) {
                return next;
            }
        }
        return null;
    }

    public Vector2 nextSquare() {
        if(layer <= 0) {
            layer = 1;
            return centre;
        }
        if(!hasNext()) {
            return null;
        }
        Vector2 next = centre.cpy();
        switch (side) {
            case 0:
                next.x += (-layer + step);
                next.y += layer;
                break;
            case 1:
                next.x += layer;
                next.y += (layer - step);
                break;
            case 2:
                next.x += (layer - step);
                next.y -= layer;
                break;
            case 3:
                next.x -= layer;
                next.y += (-layer + step);
                break;
        }
        step ++;
        if(step >= layer * 2) {
            step = 0;
            side++;
        }
        if(side >= 4) {
            side = 0;
            layer++;
        }
        return next;
    }
}
