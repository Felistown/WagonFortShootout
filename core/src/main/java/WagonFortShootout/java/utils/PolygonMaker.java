package WagonFortShootout.java.utils;

import WagonFortShootout.java.framework.entity.Hitbox;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class PolygonMaker {

    public static Polygon circle(float radius, int sides) {
        if(sides < 3) {
            throw new RuntimeException("Sides cannot be less than 3.");
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

    public static Polygon verticies(float[] array) {
        return new Polygon(array);
    }

    public static Polygon fromJson(JsonValue value) {
        String shape = value.getString("shape");
        if(shape != null) {
            switch (shape) {
                case "circle":
                    return circle(value.getFloat("radius"), value.getInt("sides"));
                case "rectangle":
                    return rectange(value.getFloat("length"), value.getFloat("height"));
                case "triangle":
                    return triangle(value.getFloat("length"), value.getFloat("height"));
                case "empty":
                    return new Polygon(new float[]{0,0,0,0,0,0});
                default:
                    throw new RuntimeException("No recognised shape known as: " + shape + ".");
            }
        }
        return new Polygon(value.asFloatArray());
    }
}
