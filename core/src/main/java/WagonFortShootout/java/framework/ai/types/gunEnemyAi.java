package WagonFortShootout.java.framework.ai.types;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.weapon.Gun;

public class gunEnemyAi extends Ai {

    boolean a = true;

    public gunEnemyAi(Entity entity) {
        super(entity);
        if(!(entity instanceof GunEntity)) {
            throw new RuntimeException(entity.getClass().getName()  + " is not instance of GunEntity.");
        }
    }

    @Override
    public void tick() {
        float goal = entity.getPos().sub((GameLevel.player.getPos().add(GameLevel.player.getVel()))).angleRad();
        entity.FACE.setGoal(goal);
    }

    @Override
    public void update() {
        Gun.Instance gun = ((GunEntity)entity).gun;
        if(a) {
            if(Utils.los(entity, GameLevel.player)) {
                gun.shoot();
            }
            if(gun.bullets() <= 0) {
                a = false;
            }
        } else {
            gun.reload();
            if(gun.bullets() >= gun.maxBullets()) {
                a = true;
            }
        }
    }
}
