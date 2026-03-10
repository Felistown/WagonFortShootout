package WagonFortShootout.java.framework.ai.types;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.ai.State;
import WagonFortShootout.java.framework.ai.StateMachine;
import WagonFortShootout.java.framework.ai.pathfinding.GridSearcher;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class gunEnemyAi extends Ai {

    public Vector2 targetPos = new Vector2(50,50);

    public gunEnemyAi(Entity entity) {
        super(entity, new StateMachine(new State[]{State.SEEK, State.RELOAD}));
        if(!(entity instanceof GunEntity)) {
            throw new RuntimeException(entity.getClass().getName()  + " is not instance of GunEntity.");
        }
    }

    @Override
    public void tick() {
        super.tick();
        float goal = entity.getPos().sub((GameLevel.player.getPos().add(GameLevel.player.getVel()))).angleRad();
        entity.FACE.setGoal(goal);
    }

    @Override
    public void update() {
        Gun.Instance gun = ((GunEntity)entity).gun;
        if(STATE.is(State.SEEK)) {
            if(moveable() && !Utils.los(targetPos, entity, GameLevel.player, Utils.closetHitBox(targetPos))) {
                hunt();
            }
            if(Utils.los(entity, GameLevel.player)) {
                gun.shoot();
            }
            if(gun.bullets() <= 0) {
                STATE.setState(State.RELOAD);
            }
        } else {
            if(moveable() && Utils.los(targetPos, entity, GameLevel.player, Utils.closetHitBox(targetPos))) {
                hide();
            }
            gun.reload();
            if(gun.bullets() >= gun.maxBullets()) {
                STATE.setState(State.SEEK);
            }
        }
    }

    public void hunt() {
        GridSearcher searcher = new GridSearcher(entity.getPos(), 100);
        Vector2 min = new Vector2(1,1);
        Vector2 max = new Vector2(99,99);
        Vector2 tpos = new Vector2(0,0);
        Hitbox[] all = Utils.closetHitBox(entity);
        float tscore = Float.MAX_VALUE;
        boolean found = false;
        while(searcher.hasNext()) {
            Vector2 cpos = searcher.nextSquare();
            if(Mth.within(cpos, min, max) && Utils.los(cpos, entity, GameLevel.player, all) && entity.HITBOX.traverable(cpos)) {
                float cscore = 1000 * cpos.dst2(entity.getPos()) + cpos.dst2(GameLevel.player.getPos());
                if(cscore < tscore) {
                    tpos = cpos;
                    tscore = cscore;
                }
                if(!found) {
                    searcher = searcher.resize(searcher.getLayer() + 1);
                    found = true;
                }
            }
        }
        goTo(tpos);
        targetPos = tpos;
        Effect.addEffect(new Effect(new Texture("image/missing_texture.png"), 1f, 1f), 6, tpos, 0, 4);
    }

    public void hide() {
        //TODO Entity will rush across sightlines, past suitable cover that are slightly farther away. Entity would hide in front of cover if player goes behind own cover. Entity will try take cover behind player
        GridSearcher searcher = new GridSearcher(entity.getPos(), 100);
        Vector2 min = new Vector2(1,1);
        Vector2 max = new Vector2(99,99);
        Vector2 tpos = new Vector2(0,0);
        Hitbox[] all = Utils.closetHitBox(entity);
        Hitbox stat = all[0];
        for(Hitbox h: all) {
            if(h.anchored) {
                stat = h;
                break;
            }
        }
        float tscore = Float.MAX_VALUE;
        boolean found = false;
        while(searcher.hasNext()) {
            Vector2 cpos = searcher.nextSquare();
            if(Mth.within(cpos, min, max) &&!Utils.los(cpos, entity, GameLevel.player, all)  && !stat.pointIntersects(entity.HITBOX, cpos)) {
                float cscore = cpos.dst2(entity.getPos()) * cpos.dst2(stat.getPosition());
                if(cscore < tscore) {
                    tpos = cpos;
                    tscore = cscore;
                }
                if(!found) {
                    searcher = searcher.resize(searcher.getLayer() + 1);
                    found = true;
                }
            }
        }
        goTo(tpos);
        targetPos = tpos;
        Effect.addEffect(new Effect(new Texture("image/missing_texture.png"), 1f, 1f), 6, tpos, 0, 4);
    }
}
