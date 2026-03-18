package WagonFortShootout.java.framework.ai.types;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.GunEntity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.image.Beam;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;
import WagonFortShootout.java.world.RayCast;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class ControllableAi extends Ai {

    private static final Beam BEAM = new Beam("effects/aim_beam", 1, -1, 0.25f);

    private int cooldown = 0;
    private Beam.Instance instance;

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
        entity.move(added);
        GameLevel.cam.setPos(entity.getPos());
        Vector2 mouse = new Vector2(GameLevel.mouse.x, GameLevel.mouse.y);
        Vector2 dif = entity.getPos().sub(mouse);
        entity.setGoal(dif.angleRad());
    }

    private void face() {
        boolean aim = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        if(aim) {
            ((GunEntity)entity).gun.inaccuracy = 0;
            Vector2 pos = entity.getPos();
            Vector2 beamPos = Mth.toVec(entity.getFacing(), 200).add(pos);
            RayCast rayCast = new RayCast(pos, beamPos);
            rayCast.enter(enter -> {
                if (enter.hitbox() != entity.HITBOX  && !(entity.mount != null && enter.hitbox().equals(entity.mount.HITBOX))) {
                    beamPos.set(enter.pos());
                    return false;
                }
                return true;
            });
            if(instance == null) {
                instance = BEAM.instance(pos, beamPos);
            } else {
                instance.point(pos, beamPos);
            }
        } else {
            if(instance != null) {
                instance.remove();
                instance = null;
            }
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

    @Override
    protected void onRemove() {
        if(instance != null) {
            instance.remove();
            instance = null;
        }
    }
}
