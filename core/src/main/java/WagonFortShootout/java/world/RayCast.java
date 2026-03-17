package WagonFortShootout.java.world;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RayCast {

    private final AmanatidesAndWoo aaw;
    private final Vector2 from;
    private final Vector2 to;

    public RayCast(Vector2 from, Vector2 to) {
        this.from = from.cpy();
        this.to = to.cpy();
        aaw = new AmanatidesAndWoo(this.from, this.to);
    }

    public void enter(Function<Intersection, Boolean> function) {
        while(aaw.hasNext) {
            GridPoint2 point = new GridPoint2();
            HashSet<Hitbox> hitboxes = aaw.next(point);
            ArrayList<Intersection> hit = new ArrayList<Intersection>();
            for(Hitbox hitbox: hitboxes) {
                Vector2 pos = new Vector2();
                if(hitbox.rayIntersection(from, to, pos) && point.equals(GameLevel.spacialHash.getIndex(pos))) {
                    hit.add(new Intersection(hitbox, pos, Mth.angleRad(from, pos)));
                }
            }
            hit.sort(Comparator.comparingDouble(inter -> inter.pos.dst2(from)));
            for(Intersection inter:hit) {
                if(!function.apply(inter)) {
                    return;
                }
            }
        }
    }

    public void enterAndExit(BiFunction<Intersection, Intersection, Boolean> function) {
        while(aaw.hasNext) {
            GridPoint2 point = new GridPoint2();
            HashSet<Hitbox> hitboxes = aaw.next(point);
            ArrayList<Intersection[]> hit = new ArrayList<Intersection[]>();
            for(Hitbox hitbox: hitboxes) {
                Vector2 enpos = new Vector2();
                if(hitbox.rayIntersection(from, to, enpos) && point.equals(GameLevel.spacialHash.getIndex(enpos))) {
                    Vector2 expos = new Vector2();
                    if(hitbox.rayIntersectionFar(from, to, expos)) {
                        float anglerad = Mth.angleRad(from, enpos);
                        Intersection[] array = new Intersection[]{new Intersection(hitbox, enpos, anglerad), new Intersection(hitbox, expos, anglerad)};
                        hit.add(array);
                    }
                }
            }
            hit.sort(Comparator.comparingDouble(inter -> inter[0].pos.dst2(from)));
            for(Intersection[] inter:hit) {
                if(!function.apply(inter[0], inter[1])) {
                    return;
                }
            }
        }
    }

    public record Intersection(Hitbox hitbox, Vector2 pos, float angleRad) {
    }

    private static class AmanatidesAndWoo {

        private float tMaxX;
        private final float tDeltaX;
        private final int stepX;
        private float tMaxY;
        private final float tDeltaY;
        private final int stepY;
        private final GridPoint2 pos;
        private boolean hasNext;

        public AmanatidesAndWoo(Vector2 from, Vector2 to) {
            hasNext = true;
            float cellHeight = GameLevel.spacialHash.cellHeight();
            float cellWidth = GameLevel.spacialHash.cellWidth();
            pos = GameLevel.spacialHash.getIndex(from);
            float dx = to.x - from.x;
            float dy = to.y - from.y;
            stepX = (dx > 0) ? 1 : -1;
            stepY = (dy > 0) ? 1 : -1;
            tDeltaX = (dx == 0) ? Float.MAX_VALUE : Math.abs(cellWidth / dx);
            tDeltaY = (dy == 0) ? Float.MAX_VALUE : Math.abs(cellHeight / dy);
            if (dx > 0) {
                tMaxX = ((pos.x + 1) * cellWidth - from.x) / dx;
            } else if (dx < 0) {
                tMaxX = (pos.x * cellWidth - from.x) / dx;
            } else {
                tMaxX = Float.POSITIVE_INFINITY;
            }
            if (dy > 0) {
                tMaxY = ((pos.y + 1) * cellHeight - from.y) / dy;
            } else if (dy < 0) {
                tMaxY = (pos.y * cellHeight - from.y) / dy;
            } else {
                tMaxY = Float.POSITIVE_INFINITY;
            }
        }

        public boolean hasNext() {
            return hasNext;
        }

        public HashSet<Hitbox> next() {
            return next(null);
        }

        public HashSet<Hitbox> next(GridPoint2 point) {
            if(hasNext) {
                HashSet<Hitbox> hitboxes = GameLevel.spacialHash.query(pos);
                if(point != null) {
                    point.set(pos.x, pos.y);
                }
                if (tMaxX < tMaxY) {
                    if (tMaxX > 1) {
                        hasNext = false;
                    } else {
                        tMaxX += tDeltaX;
                        pos.x += stepX;
                    }
                } else if (tMaxY < tMaxX){
                    if (tMaxY > 1) {
                        hasNext = false;
                    } else {
                        tMaxY += tDeltaY;
                        pos.y += stepY;
                    }
                } else {
                    if (tMaxX > 1.0f) {
                        hasNext = false;
                    } else {
                        tMaxX += tDeltaX;
                        tMaxY += tDeltaY;
                        pos.x += stepX;
                        pos.y += stepY;
                    }
                }
                if(hasNext) {
                    hasNext = GameLevel.spacialHash.hasPoint(pos);
                }
                return hitboxes;
            }
            return null;
        }
    }
}
