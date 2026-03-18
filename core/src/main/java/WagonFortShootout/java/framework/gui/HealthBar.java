package WagonFortShootout.java.framework.gui;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.ProjectileEntity;
import WagonFortShootout.java.framework.image.Beam;
import com.badlogic.gdx.utils.Array;

public class HealthBar {

    private static final float LENGTH = 3;
    private static final float WIDTH = 0.5f;
    private static final Beam FILL = new Beam("green", 5, -1, WIDTH);
    private static final Beam EMPTY = new Beam("red", 5, -1, WIDTH);
    private static final float Y_OFFSET = 1;
    private static final float X_OFFSET = 0;
    private static final Array<FillAndEmpty> Beam_instances =  new Array<FillAndEmpty>();

    public static void tick() {
        for(int i = 0; i < Beam_instances.size; i++) {
            FillAndEmpty fae = Beam_instances.get(i);
            if(fae.entity.getHealth() <= 0) {
                fae.remove();
                Beam_instances.removeValue(fae, true);
                i--;
            } else {
                fae.tick();
            }
        }
        for(Entity e: Entity.getAllEntities()) {
            if(!(e instanceof ProjectileEntity)) {
                boolean contains = false;
                for (FillAndEmpty fae : Beam_instances) {
                    if (fae.entity == e) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    new FillAndEmpty(e);
                }
            }
        }
    }

    private static class FillAndEmpty {

        public final Entity entity;
        private final Beam.Instance fill;
        private final Beam.Instance empty;

        private FillAndEmpty(Entity entity) {
            this.entity = entity;
            Beam_instances.add(this);
            fill = FILL.instance(entity.getPos().add(-LENGTH / 2 + X_OFFSET, Y_OFFSET), entity.getPos().add(LENGTH / 2 + X_OFFSET, Y_OFFSET));
            empty = EMPTY.instance(entity.getPos().add(LENGTH / 2 + X_OFFSET, Y_OFFSET), entity.getPos().add(-LENGTH / 2 + X_OFFSET, Y_OFFSET));
        }

        private void tick() {
            fill.point(entity.getPos().add(-LENGTH / 2 + X_OFFSET, Y_OFFSET), entity.getPos().add(-LENGTH / 2 + (LENGTH * ((float)entity.getHealth() / entity.MAX_HEALTH)) + X_OFFSET, Y_OFFSET));
            empty.point(entity.getPos().add(LENGTH / 2 + X_OFFSET, Y_OFFSET), entity.getPos().add(LENGTH / 2 - (LENGTH * (1 - (float)entity.getHealth() / entity.MAX_HEALTH)) + X_OFFSET, Y_OFFSET));
        }

        private void remove() {
            fill.remove();
            empty.remove();
        }
    }
}
