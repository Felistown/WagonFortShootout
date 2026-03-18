package WagonFortShootout.java.utils.mutable;

import java.io.Serializable;

public class MutableNumber extends Number implements Serializable {

    private double number;

    public <T extends Number> MutableNumber(T number) {
        this.number = number.doubleValue();
    }

    public <T extends Number> MutableNumber add(T value) {
       number += value.doubleValue();
       return this;
    }

    public <T extends Number> MutableNumber sub(T value) {
        number -= value.doubleValue();
        return this;
    }

    public <T extends Number> MutableNumber mult(T value) {
        number *= value.doubleValue();
        return this;
    }

    public <T extends Number> MutableNumber div(T value) {
        number /= value.doubleValue();
        return this;
    }

    public <T extends Number> MutableNumber mod(T value) {
        number %= value.doubleValue();
        return this;
    }

    public <T extends Number> MutableNumber set(T value) {
        number = value.doubleValue();
        return this;
    }

    public MutableNumber zero() {
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
