package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.effects.Effect;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.Bullet;
import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.utils.Face;
import WagonFortShootout.java.utils.Mth;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Octree;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    private final float SPEED = 0.1f;
    private final float SPRINT_MULT = 1.1f;

    private int cooldown = 0;

    public Player(Vector2 pos) {
        super(pos, new Sprite(new Texture("missing_texture.png")), 1);
        FACE.setSpeed(0.0174533f / 2);
    }

    @Override
    public void tick() {
        movement();
        gun();
        super.tick();
    }

    private void gun() {
        boolean shoot = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        if(shoot && cooldown <= 0) {
            Bullet bullet = new Bullet(1, this);
            bullet.shoot(POS.pos(), FACE.getFacing());
            cooldown = 50;
            float recoil = 0.5f;
            POS.addVel((float)(FACE.getFacing() + Math.PI), recoil);
            FACE.recoil(1.2f, 0.5f);
        }
        cooldown = Math.max(cooldown - 1, 0);
    }

    private void movement() {
        //movement

        boolean w = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean a = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean s = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean d = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        Vector2 added = new Vector2(0,0);
        if(!(w && s)) {
            if(w) {
                added.y = SPEED;
            } else if(s){
                added.y = -SPEED;
            }
        }
        if(!(a && d)) {
            if(a) {
                added.x = -SPEED;
            } else if(d){
                added.x = SPEED;
            }
        }
        if(shift) {
            added.x *= SPRINT_MULT;
            added.y *= SPRINT_MULT;
        } else {
        }
        POS.addVel(added);
        Vector2 mouse = new Vector2(GameLevel.mouse.x, GameLevel.mouse.y);
        Vector2 dif = POS.pos().sub(mouse);
        FACE.setGoal(dif.angleRad());
        boolean aim = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        if(aim) {
            //Beam.beam(POS.pos(), Mth.toVec(FACE.getFacing(), 150).add(POS.pos()),0.25f, 1, Color.GRAY);
            Effect effect = new Effect(new Texture("a.png"), 150, 0.25f, Mth.toVec(FACE.getFacing(), 75).add(POS.pos()), (float) Math.toDegrees(FACE.getFacing()));
            Effect.addEffect(effect, 1);
        }
    }
}
