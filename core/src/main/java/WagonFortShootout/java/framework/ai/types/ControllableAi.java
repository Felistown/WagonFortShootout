package WagonFortShootout.java.framework.ai.types;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.Player;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.ai.pathfinding.GridSearcher;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ControllableAi extends Ai {

    private static final float sprint =  0.05f;
    private static final float walk = 0.01666666666f;
    private int cooldown = 0;
    GridSearcher g = new GridSearcher(new Vector2(50,50), 10);

    public ControllableAi(Entity entity) {
        super(entity, null);
    }

    @Override
    public void tick() {
        move();
        face();
        if(entity instanceof GunEntity) {
            gun();
        }
        cooldown = Math.max(0, cooldown - 1);
    }

    @Override
    public void update() {
    }

    private void move() {
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean a = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean s = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean d = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean m = Gdx.input.isKeyJustPressed(Input.Keys.M);
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        Vector2 added = new Vector2(0,0);
        if(m && cooldown <= 0) {
            if (entity.mount != null) {
                entity.mount.dismount();
            } else {
                Entity[] b = Utils.closetEntity(entity);
                for (Entity j : b) {
                    if (Utils.dist(entity, j) < 70 && j instanceof Mount i && i.mount == null) {
                        i.mount(entity);
                        break;
                    }
                }
            }
            cooldown = 200;
        }
        if(!(w && s)) {
            if(w) {
                added.y = 1;
            } else if(s){
                added.y = -1;
            }
        }
        if(!(a && d)) {
            if(a) {
                added.x = -1;
            } else if(d){
                added.x = 1;
            }
        }
        if(shift) {
            entity.POS.max_speed = 0.05f;
        } else {
            entity.POS.max_speed = 0.01666666666f;
        }
        entity.move(added);
        Vector2 mouse = new Vector2(GameLevel.mouse.x, GameLevel.mouse.y);
        Vector2 dif = entity.getPos().sub(mouse);
        entity.setGoal(dif.angleRad());
    }

    private void face() {
        boolean aim = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        if(aim) {
            ((GunEntity)entity).gun.inaccuracy = 0;
            float face = entity.getFacing();
            Vector2 pos = entity.getPos();
            Vector2 beamPos = Mth.toVec(face, 200).add(pos);
            Hitbox[] all = Utils.closetHitBox(entity);
            for (Hitbox hitbox : all) {
                Vector2 eHit = new Vector2();
                if (hitbox != entity.HITBOX  && !(entity.mount != null && hitbox == entity.mount.HITBOX)&& hitbox.rayIntersection(pos, beamPos, eHit)) {
                    beamPos = eHit;
                }
            }
            float len = beamPos.dst(pos);
            Effect effect = new Effect(new Texture("image/effects/aim_beam.png"), len, 0.25f);
            Effect.addEffect(effect, 1,Mth.toVec(face, len / 2).add(pos), (float) Math.toDegrees(face),3);
        } else {
            ((GunEntity)entity).gun.inaccuracy = 0.3f;
        }
    }

    private void gun() {
        boolean shoot = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean r = Gdx.input.isKeyPressed(Input.Keys.R);
        if(shoot) {
            ((GunEntity)entity).gun.shoot();
        }
        if(r) {
            ((GunEntity)entity).gun.reload();
        }
    }
}
