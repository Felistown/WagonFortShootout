package WagonFortShootout.java.framework.ai.pathfinding;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import WagonFortShootout.java.framework.image.Beam;

import java.util.*;

public record Pathfinder(Vector2 start, Vector2 target, Hitbox hitbox)  {

    private static final Beam BEAM = new Beam("missing_texture",5, 300, 0.25f);
    private static final int MAX_SEARCH_SIZE = 2000;

    public Path findPath() {
        //TODO clean this up
        if (!hitbox.traverable(target)) {
            return null;
        }
        PriorityQueue<Node> open = new PriorityQueue<Node>();
        HashSet<Node> closed = new HashSet<Node>();
        Node startNode = new Node(start);
        startNode.cost = 0;
        startNode.heuristic = start.dst(target);
        startNode.function = startNode.heuristic;
        open.add(startNode);
        for (int i = 0; i <= MAX_SEARCH_SIZE && !open.isEmpty(); i++) {
            Node current = open.poll();
            if (current.POS.dst(target) <= 0.5f || i >= MAX_SEARCH_SIZE) {
                System.out.println(i);
                return new Path(current);
            }
            closed.add(current);
            for (Node n : current.neighbours()) {
                if (!closed.contains(n) && hitbox.traverable(n.POS)) {
                    float cost = current.cost + current.POS.dst(n.POS);
                    if (open.contains(n)) {
                        if (cost < n.cost) {
                            n.cost = cost;
                            n.parent = current;
                            n.heuristic = 1.1f * n.POS.dst(target);
                            n.function = n.cost + n.heuristic;
                            open.remove(n);
                            open.add(n);
                        }
                    } else {
                        n.heuristic = 1.1f * n.POS.dst(target);
                        n.function = n.cost + n.heuristic;
                        n.cost = cost;
                        n.parent = current;
                        open.add(n);
                    }
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
            while (temp.parent != null) {
                path.add(temp.parent.POS);
                temp = temp.parent;
            }
            Collections.reverse(path);

            for (int i = 0; i < path.size() - 1; i++) {
                BEAM.instance(get(i), get(i + 1));
            }


        }

        public Vector2 current() {
            if (index >= path.size()) {
                return path.getLast().cpy();
            }
            return get(index);
        }

        public boolean hasEnded() {
            return index >= path.size();
        }

        public Vector2 next() {
            Vector2 next = get(index);
            index++;
            return next;
        }

        public Vector2 get(int index) {
            if (index >= path.size()) {
                return null;
            } else {
                return path.get(index).cpy();
            }
        }

        public void follow(Entity entity) {
            if (Mth.chebDist(entity.getPos(), current()) <= 1) {
                index++;
            }
            entity.move(current().sub(entity.getPos()));
        }
    }

    private class Node implements Comparable<Node> {

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
            for (int i = 0; i < 8; i++) {
                neighbours[i] = new Node(searcher.nextSquare());
            }
            return neighbours;
        }

        @Override
        public boolean equals(Object o) {
            Vector2 oPos = ((Node)o).POS;
            return POS.x == oPos.x && POS.y == oPos.y;
        }

        @Override
        public int compareTo(Node o) {
            return Float.compare(function, o.function);
        }

        @Override
        public int hashCode() {
            return Objects.hash(POS.x, POS.y);
        }
    }
}
