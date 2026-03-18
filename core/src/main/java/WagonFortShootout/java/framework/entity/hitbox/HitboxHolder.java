package WagonFortShootout.java.framework.entity.hitbox;

public interface HitboxHolder {

    public default Class getType() {
        return this.getClass();
    }

}
