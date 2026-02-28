package WagonFortShootout.java.utils;

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
}
