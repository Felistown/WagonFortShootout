package WagonFortShootout.java.utils;

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
}
