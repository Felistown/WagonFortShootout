package WagonFortShootout.java.framework.ai;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;

public class gunEnemyAi extends Ai{
    
    boolean a = true;

    public gunEnemyAi(Entity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        super.tick();
        float goal = entity.getPOS().pos().sub((GameLevel.player.getPOS().pos().add(GameLevel.player.getPOS().vel()))).angleRad();
        entity.getFACE().setGoal(goal);
    }

    @Override
    public void update() {
        super.update();
        if(a) {
            if(Utils.los(entity, GameLevel.player)) {
                entity.getGun().shoot();
            }
            if(entity.getGun().bullets() <= 0) {
                a = false;
            }
        } else {
            entity.getGun().reload();
            if(entity.getGun().bullets() >= entity.getGun().maxBullets()) {
                a = true;
            }
        }
    }
}
