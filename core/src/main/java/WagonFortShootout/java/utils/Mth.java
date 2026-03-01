package WagonFortShootout.java.utils;

import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.world.Hitbox;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Mth {

    //returns the value if it is within the range of max and -max, max otherwise
    public static double sigmin(double max, double value) {
        if(value < 0) {
            return Math.max(-max, value);
        } else {
            return Math.min(max, value);
        }
    }

    public static double[] vector(double direction, double magintude) {
        double[] ret = new double[2];
        ret[0] = Math.cos(direction) * magintude;
        ret[1] = Math.sin(direction) * magintude;
        return ret;
    }

    public static Vector2 toVec(double  angle, float magnitude) {
        return new Vector2((float)-Math.cos(angle)  * magnitude,  (float)-Math.sin(angle) * magnitude);
    }

    public static Vector2 randomVec(float min, float max) {
        return new Vector2((float) Math.random() * max + min, (float)Math.random() * max + min);
    }

    public static float rand(float min, float max) {
        return (float) Math.random() * max + min;
    }

    public static boolean intersectRayPolygon(Vector2 p1, Vector2 p2, Polygon polygon, Vector2 hitPos) {
        float[] vertices = polygon.getTransformedVertices();
        float x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;
        int n = vertices.length;
        float x3 = vertices[n - 2], y3 = vertices[n - 1];
        for (int i = 0; i < n; i += 2) {
            float x4 = vertices[i], y4 = vertices[i + 1];
            float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
            if (d != 0) {
                float yd = y1 - y3;
                float xd = x1 - x3;
                float ua = ((x4 - x3) * yd - (y4 - y3) * xd) / d;
                if (ua >= 0 && ua <= 1) {
                    hitPos = new Vector2( (x2-x1)*ua + x1, (y2-y1)*ua + y1);
                    return true;
                }
            }
            x3 = x4;
            y3 = y4;
        }
        return false;
    }

    public static boolean intersectSegmentPolygon(Vector2 p1, Vector2 p2, Polygon polygon, Vector2 hitPos) {
        float[] vertices = polygon.getTransformedVertices();
        float x1 = p1.x;
        float y1 = p1.y;
        float x2 = p2.x;
        float y2 = p2.y;
        int n = vertices.length;
        float x3 = vertices[n - 2];
        float y3 = vertices[n - 1];

        for(int i = 0; i < n; i += 2) {
            float x4 = vertices[i];
            float y4 = vertices[i + 1];
            float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
            if (d != 0.0F) {
                float yd = y1 - y3;
                float xd = x1 - x3;
                float ua = ((x4 - x3) * yd - (y4 - y3) * xd) / d;
                if (ua >= 0.0F && ua <= 1.0F) {
                    float ub = ((x2 - x1) * yd - (y2 - y1) * xd) / d;
                    if (ub >= 0.0F && ub <= 1.0F) {
                        hitPos.set((x2-x1)*ua + x1, (y2-y1)*ua + y1);
                        return true;
                    }
                }
            }
            x3 = x4;
            y3 = y4;
        }
        return false;
    }

    public static Polygon circle(float radius, int sides) {
        if(sides < 3) {
            throw new RuntimeException("Sides cannot be less than 3");
        }
        float[] vertices = new float[sides * 2];
        for(int i = 0; i + 1 < sides * 2; i+=2) {
            float rad = (float)Math.PI * 2 - (float)Math.PI * 2 * ((float)(i / 2) / sides);
            vertices[i] = (float)Math.cos(rad) * radius;
            vertices[i + 1] = (float)Math.sin(rad) * radius;
        }
        return new Polygon(vertices);
    }

    public static Polygon rectange(float length, float height) {
        float len = length / 2;
        float hgt = height / 2;
        float[] vertices = new float[]{-len, -hgt, -len, hgt, len, hgt, len, -hgt};
        return new Polygon(vertices);
    }

    public static Polygon triangle(float length, float height) {
        float len = length / 2;
        float hgt = height / 2;
        float[] vertices = new float[]{-len, -hgt, 0, hgt, len, -hgt};
        return new Polygon(vertices);
    }
}
