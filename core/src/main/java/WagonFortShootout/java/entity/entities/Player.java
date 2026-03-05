package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.generic.GunEntity;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.framework.HitData;
import WagonFortShootout.java.framework.entity.Hitbox;
import WagonFortShootout.java.utils.Mth;
import WagonFortShootout.java.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends GunEntity {

    private int cooldown = 0;


    public Player(Vector2 pos) {
        super(pos, new Sprite(new Texture("image/circle.png")), Mth.circle( (float) 1/ 2, 8),10000,1,5, "lever_rifle");
    }

    @Override
    public void tick() {
        movement();
        gun();
        cooldown = Math.max(0, cooldown - 1);
        super.tick();
    }

    private void gun() {
        boolean shoot = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean r = Gdx.input.isKeyPressed(Input.Keys.R);
        if(shoot) {
            gun.shoot();
        }
        if(r) {
            gun.reload();
        }
    }

    private void movement() {
        //movement
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean a = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean s = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean d = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean m = Gdx.input.isKeyJustPressed(Input.Keys.M);
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        boolean aim = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        Vector2 added = new Vector2(0,0);
        if(m && cooldown <= 0) {
            if (mount != null) {
                mount.dismount();
            } else {
                Entity[] b = Utils.closetEntity(this);
                for (Entity j : b) {
                    if (Utils.dist(this, j) < 3 && j instanceof Mount i && i.mount == null) {
                        i.mount(this);
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
            POS.max_speed = 0.05f;
        } else {
            POS.max_speed = 0.01666666666f;
        }
        move(added);
        Vector2 mouse = new Vector2(GameLevel.mouse.x, GameLevel.mouse.y);
        Vector2 dif = POS.pos().sub(mouse);
        setGoal(dif.angleRad());
        if(aim) {
            gun.inaccuracy = 0;
            Effect effect = new Effect(new Texture("image/effects/aim_beam.png"), 400, 0.25f);
            Effect.addEffect(effect, 1, Mth.toVec(FACE.getFacing(), 200).add(POS.pos()), (float) Math.toDegrees(FACE.getFacing()),3);
        } else {
            gun.inaccuracy = 0.3f;
        }
    }

    @Override
    public void onHit(HitData data) {
        super.onHit(data);
        GameLevel.SCREEN_SHAKER.rumble(data.rumble / 2, 5);
    }
}
