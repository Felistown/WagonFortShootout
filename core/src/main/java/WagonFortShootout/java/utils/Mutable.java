package WagonFortShootout.java.utils;

public class Mutable <T>{

    private T value;

    public Mutable() {
    }

    public Mutable(T value) {
        this.value = value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
