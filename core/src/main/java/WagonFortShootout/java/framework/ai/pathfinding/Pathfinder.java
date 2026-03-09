package WagonFortShootout.java.framework.ai.pathfinding;

import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Pathfinder {

    private static final int MAX_SEARCH_SIZE = 100;

    private final Vector2 start;
    private final Vector2 target;
    private final Hitbox hitbox;

    public Pathfinder(Vector2 start, Vector2 target, Hitbox hitbox) {
        this.start = start;
        this.target = target;
        this.hitbox = hitbox;
    }

    public Path findPath() {
        ArrayList<Node> open = new ArrayList<Node>();
        HashSet<Node> closed = new HashSet<Node>();
        if(!hitbox.traverable(target)) {
            return null;
        }
        open.add(new Node(start));
        for(int i = 0; i <= MAX_SEARCH_SIZE && !open.isEmpty(); i ++) {
            Node current = open.getFirst();
            for(Node n: open) {
                if(n.function < current.function) {
                    current = n;
                }
            }
            if(current.POS.dst(target) < 1 || i >= MAX_SEARCH_SIZE) {
                return new Path(current);
            }
            open.remove(current);
            closed.add(current);
            for(Node n: current.neighbours()) {
                if(!closed.contains(n) && hitbox.traverable(n.POS)) {
                    float cost = current.cost + current.POS.dst(n.POS);
                    if(open.contains(n)) {
                        if(cost < n.cost) {
                            n.cost = cost;
                            n.parent = current;
                        }
                    } else {
                        n.cost = cost;
                        n.parent = current;
                        open.add(n);
                    }
                    n.heuristic = 1.5f * n.POS.dst(target);
                    n.function = n.cost + n.heuristic;
                }
            }
        }
        return null;
    }

    public static class Path {

        private final ArrayList<Vector2> path;
        private int index;

        private Path(Node node) {
            path = new ArrayList<Vector2>();
            path.add(node.POS);
            Node temp = node;
            while(temp.parent != null) {
                path.add(temp.parent.POS);
                temp = temp.parent;
            }
            Collections.reverse(path);
            for(int i = 0; i < path.size() - 1; i++) {
                Beam.beam(get(i), get(i + 1), 0.25f, 2, Color.BLUE);
            }
        }

        public Vector2 next() {
            Vector2 next = get(index);
            index++;
            return next;
        }

        public Vector2 get(int index) {
            if(index >= path.size()) {
                return null;
            } else {
                return path.get(index);
            }
        }
    }

    private class Node{

        public Node parent;
        public final Vector2 POS;
        public float cost;
        public float function;
        public float heuristic;

        public Node(Vector2 pos) {
            POS = pos;
        }

        public Node[] neighbours() {
            GridSearcher searcher = new GridSearcher(POS, 1);
            searcher.nextSquare();
            Node[] neighbours = new Node[8];
            for(int i = 0; i < 8; i ++) {
                neighbours[i] = new Node(searcher.nextSquare());
            }
            return neighbours;
        }

    }
}
