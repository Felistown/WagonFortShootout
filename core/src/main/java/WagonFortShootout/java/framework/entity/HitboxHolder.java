package WagonFortShootout.java.framework.entity;

public interface HitboxHolder {

    public default Class getType() {
        return this.getClass();
    }

}
