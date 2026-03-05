package WagonFortShootout.java.utils;

import java.io.Serializable;

public class Mutable extends Number implements Serializable {

    private double number;

    public <T extends Number> Mutable(T number) {
        this.number = number.doubleValue();
    }

    public <T extends Number> Mutable add(T value) {
       number += value.doubleValue();
       return this;
    }

    public <T extends Number> Mutable sub(T value) {
        number -= value.doubleValue();
        return this;
    }

    public <T extends Number> Mutable mult(T value) {
        number *= value.doubleValue();
        return this;
    }

    public <T extends Number> Mutable div(T value) {
        number /= value.doubleValue();
        return this;
    }

    public <T extends Number> Mutable mod(T value) {
        number %= value.doubleValue();
        return this;
    }

    public <T extends Number> Mutable set(T value) {
        number = value.doubleValue();
        return this;
    }

    public Mutable zero() {
        number = 0;
        return this;
    }

    @Override
    public int intValue() {
        return ((int) number);
    }

    @Override
    public long longValue() {
        return ((long) number);
    }

    @Override
    public float floatValue() {
        return ((float) number);
    }

    @Override
    public double doubleValue() {
        return number;
    }

    @Override
    public String toString() {
        if(number % 1 <= 0) {
            return "" + intValue();
        }
        return "" + number;
    }
}
